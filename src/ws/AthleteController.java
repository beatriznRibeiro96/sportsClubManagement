package ws;

import dtos.*;
import ejbs.AthleteBean;
import ejbs.EmailBean;
import entities.*;
import exceptions.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/athletes")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AthleteController {
    @EJB
    private AthleteBean athleteBean;

    @Context
    private SecurityContext securityContext;

    public static List<AthleteDTO> toDTOs(Set<Athlete> athletes) {
        return athletes.stream().map(AthleteController::toDTO).collect(Collectors.toList());
    }

    // Converts an entity Athlete to a DTO Athlete class
    public static AthleteDTO toDTO(Athlete athlete){
        AthleteDTO athleteDTO = new AthleteDTO(
                athlete.getUsername(),
                athlete.getPassword(),
                athlete.getName(),
                athlete.getEmail(),
                athlete.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        athleteDTO.setSportSubscriptions(SportSubscriptionController.toDTOs(athlete.getSportSubscriptions()));
        athleteDTO.setGrades(GradeController.toDTOs(athlete.getGrades()));
        return athleteDTO;
    }

    // Converts an entity Athlete to a DTO Athlete class
    private AthleteDTO toDTONoSports(Athlete athlete){
        return new AthleteDTO(
                athlete.getUsername(),
                athlete.getPassword(),
                athlete.getName(),
                athlete.getEmail(),
                athlete.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<AthleteDTO> toDTOsNoSports(List<Athlete> athletes){
        return athletes.stream().map(this::toDTONoSports).collect(Collectors.toList());
    }


    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/athletes/”
    public Response all() {
        return Response.status(200).entity(toDTOsNoSports(athleteBean.all())).build();
    }

    @GET
    @Path("{username}")
    public Response getAtheleteDetails(@PathParam("username") String username) {
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
            String msg;
            try {
                Athlete athlete = athleteBean.find(username);
                if (athlete != null) {
                    return Response.status(Response.Status.OK)
                            .entity(toDTO(athlete))
                            .build();
                }
                msg = "ERROR_FINDING_ATHLETE";
                System.err.println(msg);
            } catch (Exception e) {
                msg = "ERROR_FETCHING_ATHLETE_DETAILS --->" + e.getMessage();
                System.err.println(msg);
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @POST
    @Path("/")
    public Response createNewAthlete (AthleteDTO athleteDTO) throws MyEntityExistsException, MyConstraintViolationException, MyParseDateException {
        Athlete athlete = athleteBean.create(athleteDTO.getUsername(),
                athleteDTO.getPassword(),
                athleteDTO.getName(),
                athleteDTO.getEmail(),
                athleteDTO.getBirthDate());
        return Response.status(Response.Status.OK).entity(toDTO(athlete)).build();
    }

    @PUT
    @Path("{username}")
    public Response updateAthlete(@PathParam("username") String username, AthleteDTO athleteDTO) throws MyEntityNotFoundException, MyParseDateException {
        if(securityContext.isUserInRole("Administrator")) {
            Athlete athlete = athleteBean.update(username,
                    athleteDTO.getPassword(),
                    athleteDTO.getName(),
                    athleteDTO.getEmail(),
                    athleteDTO.getBirthDate());
            return Response.status(Response.Status.OK).entity(toDTO(athlete)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAthlete (@PathParam("username") String username) throws MyEntityNotFoundException{
        athleteBean.delete(username);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{username}/sportSubscriptions")
    public Response getAthleteSportSubscriptions(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            if (athlete != null) {
                GenericEntity<List<SportSubscriptionDTO>> entity
                        = new GenericEntity<List<SportSubscriptionDTO>>(SportSubscriptionController.toDTOs(athlete.getSportSubscriptions())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_SPORT_SUBSCRIPTIONS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/grades")
    public Response getAthleteGrades(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            if (athlete != null) {
                GenericEntity<List<GradeDTO>> entity
                        = new GenericEntity<List<GradeDTO>>(GradeController.toDTOs(athlete.getGrades())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_GRADES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/activeSports")
    public Response getAthleteActiveSports(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            Set<ActiveSport> activeSports = new LinkedHashSet<>();
            if (athlete != null) {
                for (SportSubscription sportSubscription:athlete.getSportSubscriptions()) {
                    activeSports.add(sportSubscription.getRank().getActiveSport());
                }
                GenericEntity<List<ActiveSportDTO>> entity
                        = new GenericEntity<List<ActiveSportDTO>>(ActiveSportController.toDTOs(activeSports)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_ACTIVE_SPORTS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/ranks")
    public Response getAthleteRanks(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            Set<Rank> ranks = new LinkedHashSet<>();
            if (athlete != null) {
                for (SportSubscription sportSubscription:athlete.getSportSubscriptions()) {
                    ranks.add(sportSubscription.getRank());
                }
                GenericEntity<List<RankDTO>> entity
                        = new GenericEntity<List<RankDTO>>(RankController.toDTOs(ranks)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_RANKS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/schedules")
    public Response getAthleteSchedules(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            Set<Schedule> schedules = new LinkedHashSet<>();
            Set<Rank> ranks = new LinkedHashSet<>();
            if (athlete != null) {
                for (SportSubscription sportSubscription:athlete.getSportSubscriptions()) {
                    ranks.add(sportSubscription.getRank());
                }
                for (Rank rank:ranks) {
                    schedules.addAll(rank.getSchedules());
                }
                GenericEntity<List<ScheduleDTO>> entity
                        = new GenericEntity<List<ScheduleDTO>>(ScheduleController.toDTOs(schedules)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_SCHEDULES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/coaches")
    public Response getAthleteCoaches(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            Set<Coach> coaches = new LinkedHashSet<>();
            Set<Rank> ranks = new LinkedHashSet<>();
            if (athlete != null) {
                for (SportSubscription sportSubscription:athlete.getSportSubscriptions()) {
                    ranks.add(sportSubscription.getRank());
                }
                for (Rank rank:ranks) {
                    coaches.addAll(rank.getCoaches());
                }
                GenericEntity<List<CoachDTO>> entity
                        = new GenericEntity<List<CoachDTO>>(CoachController.toDTOs(coaches)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_COACHES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/messages")
    public Response getAthleteMessages(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            Set<Coach> coaches = new LinkedHashSet<>();
            Set<Rank> ranks = new LinkedHashSet<>();
            if (athlete != null) {
                GenericEntity<List<MessageDTO>> entity
                        = new GenericEntity<List<MessageDTO>>(MessageController.toDTOs(athlete.getMessages())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_MESSAGES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
