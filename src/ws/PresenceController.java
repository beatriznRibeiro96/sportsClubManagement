package ws;



import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


@Path("/presence") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class PresenceController {

    @EJB
    private PresenceBean presenceBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @RolesAllowed({"Administrator"})
    public List<PresenceDTO> all() {
        return toDTOs(presenceBean.all());
    }

    @GET
    @Path("training/{code}")
    public List<PresenceDTO> getPresencesTraining(@PathParam("code") String code) throws MyEntityNotFoundException {
        return toDTOs(presenceBean.findPresencesTraining(code));
    }

    @POST
    @Path("/")
    public Response createNewPresence(PresenceDTO presenceDTO){
        Presence presence = presenceBean.create(
                presenceDTO.getCode(),
                presenceDTO.getCodeTraining(),
                presenceDTO.getDataTraining().get(Calendar.DAY_OF_MONTH),
                presenceDTO.getDataTraining().get(Calendar.MONTH),
                presenceDTO.getDataTraining().get(Calendar.YEAR),
                presenceDTO.getAthletesPresentes(),
                presenceDTO.getEmailCoach()
        );
        return Response.status(Response.Status.CREATED).entity(toDTO(presence)).build();
    }

    @PUT
    @Path("{code}")
    public Response updatePresence(@PathParam("code") String code,PresenceDTO presenceDTO) throws MyEntityNotFoundException {
        presenceBean.update(
                presenceDTO.getCode(),
                presenceDTO.getCodeTraining(),
                presenceDTO.getDataTraining().get(Calendar.DAY_OF_MONTH),
                presenceDTO.getDataTraining().get(Calendar.MONTH),
                presenceDTO.getDataTraining().get(Calendar.YEAR),
                presenceDTO.getAthletesPresentes(),
                presenceDTO.getEmailCoach()
        );
        Presence presence = presenceBean.findPresence(code);
        return Response.status(Response.Status.OK).entity(toDTO(presence)).build();
    }

    @DELETE
    @Path("{code}")
    @RolesAllowed({"Administrator"})
    public Response removePresence(@PathParam("code") String code) throws MyEntityNotFoundException {
        presenceBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{code}")
    public Response getPresenceDetails(@PathParam("code") String code){
        Principal principal = securityContext.getUserPrincipal();
        System.out.println(code + " --- " + principal.getName());
        Presence presence = presenceBean.findPresence(code);
        if(securityContext.isUserInRole("Administrador") ||
                securityContext.isUserInRole("Training") && principal.getName().equals(presence.getTraining().getNome())) {
            return Response.status(Response.Status.OK)
                    .entity(toDTO(presence))
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();

    }

    PresenceDTO toDTO(Presence presence) {
        PresenceDTO presenceDTO = new PresenceDTO(
                presence.getCode(),
                presence.getTraining().getCode(),
                presence.getDataTraining(),
                presence.getCoach().getEmail()

        );
        List<String> athleteDTOS = athleteToDTOs(presence.getAthletesPresentes());
        presenceDTO.setAthletesPresentes(athleteDTOS);
        return presenceDTO;
    }

    List<PresenceDTO> toDTOs(List<Presence> presences) {
        return presences.stream().map(this::toDTO).collect(Collectors.toList());
    }

    String athleteToDTO(Athlete athlete) {
        return athlete.getEmail();
    }

    List<String> athleteToDTOs(List<Athlete> athletes) {
        return athletes.stream().map(this::athleteToDTOs).collect(Collectors.toList());
    }
}