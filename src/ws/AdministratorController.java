package ws;

import dtos.AdministratorDTO;
import ejbs.AdministratorBean;
import entities.Administrator;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyParseDateException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/administrators")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AdministratorController {
    @EJB
    private AdministratorBean administratorBean;
    @Context
    private SecurityContext securityContext;

    private AdministratorDTO toDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getUsername(),
                administrator.getPassword(),
                administrator.getName(),
                administrator.getEmail(),
                administrator.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<AdministratorDTO> toDTOs(List<Administrator> administrators) {
        return administrators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/administrators/”
    public Response all() {
        return Response.status(200).entity(toDTOs(administratorBean.all())).build();
    }

    @GET
    @Path("{username}")
    public Response getAdministratorDetails(@PathParam("username") String username) {
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator") || principal.getName().equals(username)) {
            String msg;
            try {
                Administrator administrator = administratorBean.find(username);
                if (administrator != null) {
                    AdministratorDTO administratorDTO = toDTO(administrator);
                    return Response.status(Response.Status.OK)
                            .entity(toDTO(administrator))
                            .build();
                }
                msg = "ERROR_FINDING_ADMINISTRATOR";
                System.err.println(msg);
            } catch (Exception e) {
                msg = "ERROR_FETCHING_ADMINISTRATOR_DETAILS --->" + e.getMessage();
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
    public Response createNewAdministrator (AdministratorDTO administratorDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MyParseDateException {
        Administrator administrator = administratorBean.create(administratorDTO.getUsername(), administratorDTO.getPassword(), administratorDTO.getName(), administratorDTO.getEmail(), administratorDTO.getBirthDate());
        return Response.status(Response.Status.OK).entity(toDTO(administrator)).build();
    }

    @PUT
    @Path("{username}")
    public Response updateAdministrator(@PathParam("username") String username, AdministratorDTO administratorDTO) throws MyEntityNotFoundException, MyParseDateException {
        if(securityContext.isUserInRole("Administrator")) {
            Administrator administrator = administratorBean.update(username,
                    administratorDTO.getPassword(),
                    administratorDTO.getName(),
                    administratorDTO.getEmail(),
                    administratorDTO.getBirthDate());
            return Response.status(Response.Status.OK).entity(toDTO(administrator)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAdministrator (@PathParam("username") String username) throws MyEntityNotFoundException{
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator") && !principal.getName().equals(username)) {
            administratorBean.delete(username);
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot delete yourself").build();
    }
}
