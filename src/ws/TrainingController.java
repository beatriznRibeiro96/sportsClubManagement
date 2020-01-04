package ws;

import dtos.AthleteDTO;
import dtos.TrainingDTO;
import ejbs.TrainingBean;
import entities.Athlete;
import entities.SportSubscription;
import entities.Training;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/trainings") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class TrainingController {
    @EJB
    private TrainingBean trainingBean;
    @Context
    private SecurityContext securityContext;

    public static TrainingDTO toDTO(Training training){
        return new TrainingDTO(
                training.getCode(),
                training.getName(),
                training.getRank().getCode(),
                training.getRank().getName(),
                training.getSchedule().getCode(),
                training.getSchedule().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<TrainingDTO> toDTOs(Collection<Training> trainings){
        return trainings.stream().map(TrainingController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/trainings/”
    public Response all() {
        return Response.status(200).entity(toDTOs(trainingBean.all())).build();
    }

    @GET
    @Path("{code}")
    public Response getTrainingDetails(@PathParam("code") int code) {
        if(securityContext.isUserInRole("Administrator") || securityContext.isUserInRole("Coach")) {
            String msg;
            try {
                Training training = trainingBean.find(code);
                if (training != null) {
                    return Response.status(Response.Status.OK)
                            .entity(toDTO(training))
                            .build();
                }
                msg = "ERROR_FINDING_TRAINING";
                System.err.println(msg);
            } catch (Exception e) {
                msg = "ERROR_FETCHING_TRAINING_DETAILS --->" + e.getMessage();
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
    public Response createNewTraining (TrainingDTO trainingDTO) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        Training training = trainingBean.create(trainingDTO.getName(), trainingDTO.getRankCode(), trainingDTO.getScheduleCode());
        return Response.status(Response.Status.CREATED).entity(toDTO(training)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateTraining(@PathParam("code") int code, TrainingDTO trainingDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        if(securityContext.isUserInRole("Administrator")) {
            Training training = trainingBean.update(code,
                    trainingDTO.getName(),
                    trainingDTO.getRankCode(),
                    trainingDTO.getScheduleCode());
            return Response.status(Response.Status.OK).entity(toDTO(training)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @PUT
    @Path("{code}/add/{username}")
    public Response addPresence(@PathParam("username") String username, @PathParam("code") int code) throws MyEntityNotFoundException, MyIllegalArgumentException {
        trainingBean.addPresenceToTraining(username, code);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{code}/remove/{username}")
    public Response removePresence(@PathParam("username") String username, @PathParam("code") int code) throws MyEntityNotFoundException {
        trainingBean.removePresenceFromTraining(username, code);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteTraining (@PathParam("code") int code) throws MyEntityNotFoundException{
        trainingBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{code}/presences")
    public Response getTrainingPresences(@PathParam("code") int code) {
        String msg;
        try {
            Training training = trainingBean.find(code);
            if (training != null) {
                Set<Athlete> presences = new LinkedHashSet<>(training.getPresences());
                GenericEntity<List<AthleteDTO>> entity
                        = new GenericEntity<List<AthleteDTO>>(AthleteController.toDTOs(presences)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_TRAINING";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_TRAINING_PRESENCES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/noPresences")
    public Response getTrainingNoPresences(@PathParam("code") int code) {
        String msg;
        try {
            Training training = trainingBean.find(code);
            if (training != null) {
                Set<Athlete> presences = new LinkedHashSet<>(training.getPresences());
                Set<Athlete> noPresences = new LinkedHashSet<>();
                for (SportSubscription sportSubscription:training.getRank().getSportSubscriptions()) {
                    noPresences.add(sportSubscription.getAthlete());
                }
                noPresences.removeAll(presences);
                GenericEntity<List<AthleteDTO>> entity
                        = new GenericEntity<List<AthleteDTO>>(AthleteController.toDTOs(noPresences)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_TRAINING";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_TRAINING_NO_PRESENCES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
