package ws;

import dtos.*;
import ejbs.CoachBean;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyParseDateException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/coaches")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class CoachController {
    @EJB
    private CoachBean coachBean;

    @Context
    private SecurityContext securityContext;

    public static List<CoachDTO> toDTOs(Set<Coach> coaches) {
        return coaches.stream().map(CoachController::toDTO).collect(Collectors.toList());
    }

    // Converts an entity Coach to a DTO Coach class
    public static CoachDTO toDTO(Coach coach){
        CoachDTO coachDTO = new CoachDTO(
                coach.getUsername(),
                coach.getPassword(),
                coach.getName(),
                coach.getEmail(),
                coach.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        coachDTO.setRanks(RankController.toDTOs(coach.getRanks()));
        return coachDTO;
    }

    // Converts an entity Coach to a DTO Coach class
    private CoachDTO toDTONoActiveSports(Coach coach){
        return new CoachDTO(
                coach.getUsername(),
                coach.getPassword(),
                coach.getName(),
                coach.getEmail(),
                coach.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<CoachDTO> toDTOsNoActiveSports(List<Coach> coaches){
        return coaches.stream().map(this::toDTONoActiveSports).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/coaches/”
    public Response all() {
        return Response.status(200).entity(toDTOsNoActiveSports(coachBean.all())).build();
    }

    @GET
    @Path("{username}")
    public Response getCoachDetails(@PathParam("username") String username) {
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
            String msg;
            try {
                Coach coach = coachBean.find(username);
                if (coach != null) {
                    return Response.status(Response.Status.OK)
                            .entity(toDTO(coach))
                            .build();
                }
                msg = "ERROR_FINDING_COACH";
                System.err.println(msg);
            } catch (Exception e) {
                msg = "ERROR_FETCHING_COACH_DETAILS --->" + e.getMessage();
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
    public Response createNewCoach (CoachDTO coachDTO) throws MyEntityExistsException, MyConstraintViolationException, MyParseDateException {
        Coach coach = coachBean.create(coachDTO.getUsername(),
                coachDTO.getPassword(),
                coachDTO.getName(),
                coachDTO.getEmail(),
                coachDTO.getBirthDate());
        return Response.status(Response.Status.CREATED).entity(toDTO(coach)).build();
    }

    @PUT
    @Path("{username}")
    public Response updateCoach(@PathParam("username") String username, CoachDTO coachDTO) throws MyEntityNotFoundException, MyParseDateException {
        if(securityContext.isUserInRole("Administrator")) {
            Coach coach = coachBean.update(username,
                    coachDTO.getPassword(),
                    coachDTO.getName(),
                    coachDTO.getEmail(),
                    coachDTO.getBirthDate());
            return Response.status(Response.Status.OK).entity(toDTO(coach)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteCoach (@PathParam("username") String username) throws MyEntityNotFoundException{
        coachBean.delete(username);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{username}/ranks")
    public Response getCoachRanks(@PathParam("username") String username) {
        String msg;
        try {
            Coach coach = coachBean.find(username);
            if (coach != null) {
                GenericEntity<List<RankDTO>> entity
                        = new GenericEntity<List<RankDTO>>(RankController.toDTOs(coach.getRanks())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_COACH";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_COACH_RANKS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/activeSports")
    public Response getCoachActiveSports(@PathParam("username") String username) {
        String msg;
        try {
            Coach coach = coachBean.find(username);
            Set<ActiveSport> activeSports = new LinkedHashSet<>();
            if (coach != null) {
                for (Rank rank:coach.getRanks()) {
                    activeSports.add(rank.getActiveSport());
                }
                GenericEntity<List<ActiveSportDTO>> entity
                        = new GenericEntity<List<ActiveSportDTO>>(ActiveSportController.toDTOs(activeSports)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_COACH";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_COACH_ACTIVE_SPORTS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/schedules")
    public Response getCoachSchedules(@PathParam("username") String username) {
        String msg;
        try {
            Coach coach = coachBean.find(username);
            Set<Schedule> schedules = new LinkedHashSet<>();
            if (coach != null) {
                for (Rank rank:coach.getRanks()) {
                    schedules.addAll(rank.getSchedules());
                }
                GenericEntity<List<ScheduleDTO>> entity
                        = new GenericEntity<List<ScheduleDTO>>(ScheduleController.toDTOs(schedules)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_COACH";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_COACH_SCHEDULES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{username}/athletes")
    public Response getCoachAthletes(@PathParam("username") String username) {
        String msg;
        try {
            Coach coach = coachBean.find(username);
            Set<Athlete> athletes = new LinkedHashSet<>();
            Set<SportSubscription> sportSubscriptions = new LinkedHashSet<>();
            if (coach != null) {
                for (Rank rank:coach.getRanks()) {
                    sportSubscriptions.addAll(rank.getSportSubscriptions());
                }
                for (SportSubscription sportSubscription:sportSubscriptions) {
                    athletes.add(sportSubscription.getAthlete());
                }
                GenericEntity<List<AthleteDTO>> entity
                        = new GenericEntity<List<AthleteDTO>>(AthleteController.toDTOs(athletes)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_COACH";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_COACH_ATHLETES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
