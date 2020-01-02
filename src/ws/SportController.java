package ws;

import dtos.SportDTO;
import ejbs.SportBean;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sports") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class SportController {
    @EJB
    private SportBean sportBean;
    @Context
    private SecurityContext securityContext;

    public static SportDTO toDTO(Sport sport){
        return new SportDTO(
                sport.getCode(),
                sport.getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<SportDTO> toDTOs(Collection<Sport> sports){
        return sports.stream().map(SportController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/sports/”
    public Response all() {
        return Response.status(200).entity(toDTOs(sportBean.all())).build();
    }

    @GET
    @Path("{code}")
    public Response getSportDetails(@PathParam("code") int code) {
        if(securityContext.isUserInRole("Administrator")) {
            String msg;
            try {
                Sport sport = sportBean.find(code);
                if (sport != null) {
                    return Response.status(Response.Status.OK)
                            .entity(toDTO(sport))
                            .build();
                }
                msg = "ERROR_FINDING_SPORT";
                System.err.println(msg);
            } catch (Exception e) {
                msg = "ERROR_FETCHING_SPORT_DETAILS --->" + e.getMessage();
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
    public Response createNewSport (SportDTO sportDTO) throws MyEntityExistsException, MyConstraintViolationException {
        Sport sport = sportBean.create(sportDTO.getName());
        return Response.status(Response.Status.CREATED).entity(toDTO(sport)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateSport(@PathParam("code") int code, SportDTO sportDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        if(securityContext.isUserInRole("Administrator")) {
            Sport sport = sportBean.update(code,
                    sportDTO.getName());
            return Response.status(Response.Status.OK).entity(toDTO(sport)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteSport (@PathParam("code") int code) throws MyEntityNotFoundException{
        sportBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
}
