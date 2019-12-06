package ws;

import dtos.AdministratorDTO;
import ejbs.AdministratorBean;
import entities.Administrator;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/administrators")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AdministratorController {
    @EJB
    private AdministratorBean administratorBean;

    private AdministratorDTO toDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getUsername(),
                administrator.getPassword(),
                administrator.getName(),
                administrator.getEmail()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private Collection<AdministratorDTO> toDTOs(Collection<Administrator> administrators) {
        return administrators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/administrators// /”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOs(administratorBean.all())).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_ADMINISTRATORS", e);
        }
    }

    @GET
    @Path("{username}")
    public Response getAdministratorDetails(@PathParam("username") String username) {
        String msg;
        try {
            Administrator administrator = administratorBean.find(username);
            if (administrator != null) {
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

    @POST
    @Path("/")
    public Response createNewAdministrator (AdministratorDTO administratorDTO) throws MyEntityExistsException, MyEntityNotFoundException {

        administratorBean.create(administratorDTO.getUsername(), administratorDTO.getPassword(), administratorDTO.getName(), administratorDTO.getEmail());
        try{
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_STUDENT", e);
        }
    }

    @PUT
    @Path("{username}")
    public Response updateAdministrator(@PathParam("username") String username, AdministratorDTO administratorDTO) throws MyEntityNotFoundException{
        Administrator administrator = administratorBean.update(username,
                administratorDTO.getPassword(),
                administratorDTO.getName(),
                administratorDTO.getEmail());
        return Response.status(Response.Status.OK).entity(toDTO(administrator)).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAdministrator (@PathParam("username") String username) throws MyEntityNotFoundException{
        administratorBean.delete(username);
        return Response.status(Response.Status.OK).build();
    }
}
