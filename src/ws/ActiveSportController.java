package ws;

import dtos.*;
import ejbs.ActiveSportBean;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Path("/activeSports") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class ActiveSportController {
    @EJB
    private ActiveSportBean activeSportBean;
    @Context
    private SecurityContext securityContext;

    public static ActiveSportDTO toDTO(ActiveSport activeSport){
        return new ActiveSportDTO(
                activeSport.getCode(),
                activeSport.getName(),
                activeSport.getSport().getCode(),
                activeSport.getSport().getName(),
                activeSport.getSeason().getCode(),
                activeSport.getSeason().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<ActiveSportDTO> toDTOs(Collection<ActiveSport> activeSports){
        return activeSports.stream().map(ActiveSportController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/activeSports/”
    public Response all() {
        return Response.status(200).entity(toDTOs(activeSportBean.all())).build();
    }

    @GET
    @Path("{code}")
    public Response getActiveSportDetails(@PathParam("code") int code) {
        if(securityContext.isUserInRole("Administrator")){
            String msg;
            try {
                ActiveSport activeSport = activeSportBean.find(code);
                if (activeSport != null) {
                    return Response.status(Response.Status.OK)
                            .entity(toDTO(activeSport))
                            .build();
                }
                msg = "ERROR_FINDING_ACTIVE_SPORT";
                System.err.println(msg);
            } catch (Exception e) {
                msg = "ERROR_FETCHING_ACTIVE_SPORT_DETAILS --->" + e.getMessage();
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
    public Response createNewActiveSport (ActiveSportDTO activeSportDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        ActiveSport activeSport = activeSportBean.create(activeSportDTO.getName(),
                activeSportDTO.getSportCode(),
                activeSportDTO.getSeasonCode());
        return Response.status(Response.Status.CREATED).entity(toDTO(activeSport)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateActiveSport(@PathParam("code") int code, ActiveSportDTO activeSportDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        if(securityContext.isUserInRole("Administrator")) {
            ActiveSport activeSport = activeSportBean.update(code,
                    activeSportDTO.getName(),
                    activeSportDTO.getSportCode(),
                    activeSportDTO.getSeasonCode());
            return Response.status(Response.Status.OK).entity(toDTO(activeSport)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteActiveSport (@PathParam("code") int code) throws MyEntityNotFoundException{
        activeSportBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{code}/coaches")
    public Response getActiveSportCoaches(@PathParam("code") int code) {
        String msg;
        try {
            ActiveSport activeSport = activeSportBean.find(code);
            Set<Coach> coaches = new LinkedHashSet<>();
            if (activeSport != null) {
                for (Rank rank:activeSport.getRanks()) {
                    coaches.addAll(rank.getCoaches());
                }
                GenericEntity<List<CoachDTO>> entity
                        = new GenericEntity<List<CoachDTO>>(CoachController.toDTOs(coaches)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ACTIVE_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ACTIVE_SPORT_COACHES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/ranks")
    public Response getActiveSportRanks(@PathParam("code") int code) {
        String msg;
        try {
            ActiveSport activeSport = activeSportBean.find(code);
            if (activeSport != null) {
                GenericEntity<List<RankDTO>> entity
                        = new GenericEntity<List<RankDTO>>(RankController.toDTOs(activeSport.getRanks())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ACTIVE_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ACTIVE_SPORT_RANKS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/grades")
    public Response getActiveSportGrades(@PathParam("code") int code) {
        String msg;
        try {
            ActiveSport activeSport = activeSportBean.find(code);
            if (activeSport != null) {
                GenericEntity<List<GradeDTO>> entity
                        = new GenericEntity<List<GradeDTO>>(GradeController.toDTOs(activeSport.getGrades())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ACTIVE_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ACTIVE_SPORT_GRADES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/schedules")
    public Response getActiveSportSchedules(@PathParam("code") int code) {
        String msg;
        try {
            ActiveSport activeSport = activeSportBean.find(code);
            Set<Schedule> schedules = new LinkedHashSet<>();
            if (activeSport != null) {
                for (Rank rank:activeSport.getRanks()) {
                    schedules.addAll(rank.getSchedules());
                }
                GenericEntity<List<ScheduleDTO>> entity
                        = new GenericEntity<List<ScheduleDTO>>(ScheduleController.toDTOs(schedules)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ACTIVE_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ACTIVE_SPORT_SCHEDULES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/athletes")
    public Response getActiveSportAthletes(@PathParam("code") int code) {
        String msg;
        try {
            ActiveSport activeSport = activeSportBean.find(code);
            Set<Athlete> athletes = new LinkedHashSet<>();
            Set<SportSubscription> sportSubscriptions = new LinkedHashSet<>();
            if (activeSport != null) {
                for (Rank rank:activeSport.getRanks()) {
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
            msg = "ERROR_FINDING_ACTIVE_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ACTIVE_SPORT_ATHLETES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
