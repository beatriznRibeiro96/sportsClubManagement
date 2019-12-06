package ws;

import dtos.AdministratorDTO;
import dtos.AthleteDTO;
import ejbs.AthleteBean;
import entities.Administrator;
import entities.Athlete;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/athletes")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AthleteController {
    @EJB
    private AthleteBean athleteBean;

    private AthleteDTO toDTO(Athlete athlete) {
        return new AthleteDTO(
                athlete.getUsername(),
                athlete.getPassword(),
                athlete.getName(),
                athlete.getEmail()
        );
    }
    private Collection<AthleteDTO> toDTOs(Collection<Athlete> athletes) {
        return athletes.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/administrators// /”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOs(athleteBean.all())).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_ATHLETES", e);
        }
    }
    @GET
    @Path("{username}")
    public Response getAtheleteDetails(@PathParam("username") String username) {
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

    @POST
    @Path("/")
    public Response createNewAthlete (AthleteDTO athleteDTO) throws MyEntityExistsException, MyEntityNotFoundException {

        athleteBean.create(athleteDTO.getUsername(), athleteDTO.getPassword(), athleteDTO.getName(), athleteDTO.getEmail());
        try{
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_ATHLETE", e);
        }
    }

    @PUT
    @Path("{username}")
    public Response updateAthlete(@PathParam("username") String username, AthleteDTO athleteDTO) throws MyEntityNotFoundException{
        Athlete athlete = athleteBean.update(username,
                athleteDTO.getPassword(),
                athleteDTO.getName(),
                athleteDTO.getEmail());
        return Response.status(Response.Status.OK).entity(toDTO(athlete)).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAthlete (@PathParam("username") String username) throws MyEntityNotFoundException{
        athleteBean.delete(username);
        return Response.status(Response.Status.OK).build();
    }

}
