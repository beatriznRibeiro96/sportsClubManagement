package ws;


import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/trainings")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TrainingController {

    @EJB
    private TrainingBean trainingBean;

    @Context
    private SecurityContext securityContext;

    Training toDTO(Training training) {
        return new TrainingDTO(
                training.getCode(),
                training.getEmailCoach(),
                training.getCodeGrade(),
                training.getHoraInicio(),
                training.getHoraFim(),
                training.getDiaSemana()
        );
    }

    List<TrainingDTO> toDTOs(List<Training> training) {
        return training.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/trainings/”
    public List<TrainingDTO> all(){
        return toDTOs(trainingBean.all());
    }

    @POST
    @Path("/")
    @RolesAllowed({"Administrator"})
    public Response createNewTraining(TrainingDTO trainingDTO){
        Principal principal = securityContext.getUserPrincipal();
        System.out.println("Training: " + trainingDTO.getCode() + " --- " + principal.getName());

        if(securityContext.isUserInRole("Administrador")) {
            Training training = trainingBean.create(
                    trainingDTO.getCode(),
                    trainingDTO.getEmailCoach(),

                    trainingDTO.getCodeGrade(),

                    trainingDTO.getHoraInicio(),
                    trainingDTO.getHoraFim(),
                    trainingDTO.getDiaSemana()
            );
            return Response.status(Response.Status.CREATED).entity(toDTO(training)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @PUT
    @Path("{code}")
    @RolesAllowed({"Administrator"})
    public Response updateTraining(@PathParam("code") String code, TrainingDTO trainingDTO) throws MyEntityNotFoundException {
        Principal principal = securityContext.getUserPrincipal();
        System.out.println("Training: " + trainingDTO.getCode() + " --- " + principal.getName());
        if(securityContext.isUserInRole("Administrador")) {
            trainingBean.update(
                    code,
                    trainingDTO.getEmailCoach(),

                    trainingDTO.getCodeGrade(),

                    trainingDTO.getHoraInicio(),
                    trainingDTO.getHoraFim(),
                    trainingDTO.getDiaSemana()
            );
            Training training = trainingBean.findTraining(code);
            return Response.status(Response.Status.OK)
                    .entity(toDTO(training))
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("{code}")
    @RolesAllowed({"Administrator"})
    public Response removeTraining (@PathParam("code") String code){
        trainingBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
}