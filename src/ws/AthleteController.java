package ws;

import dtos.AthleteDTO;
import dtos.SportDTO;
import ejbs.AthleteBean;
import entities.Athlete;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/athletes")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AthleteController {
    @EJB
    private AthleteBean athleteBean;

    public static List<AthleteDTO> toDTOs(Set<Athlete> athletes) {
        return athletes.stream().map(AthleteController::toDTO).collect(Collectors.toList());
    }

    // Converts an entity Athlete to a DTO Athlete class
    public static AthleteDTO toDTO(Athlete athlete){
        AthleteDTO athleteDTO = new AthleteDTO(
                athlete.getUsername(),
                athlete.getPassword(),
                athlete.getName(),
                athlete.getEmail()
        );

        athleteDTO.setSports(SportController.toDTOs(athlete.getSports()));
        return athleteDTO;
    }

    // Converts an entity Athlete to a DTO Athlete class
    private AthleteDTO toDTONoSports(Athlete athlete){
        return new AthleteDTO(
                athlete.getUsername(),
                athlete.getPassword(),
                athlete.getName(),
                athlete.getEmail()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<AthleteDTO> toDTOsNoSports(List<Athlete> athletes){
        return athletes.stream().map(this::toDTONoSports).collect(Collectors.toList());
    }


    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/administrators// /”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOsNoSports(athleteBean.all())).build();
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
    public Response createNewAthlete (AthleteDTO athleteDTO) throws MyEntityExistsException {
        Athlete athlete = athleteBean.create(athleteDTO.getUsername(),
                athleteDTO.getPassword(),
                athleteDTO.getName(),
                athleteDTO.getEmail());
        return Response.status(Response.Status.OK).entity(toDTO(athlete)).build();
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

    @GET
    @Path("{username}/sports")
    public Response getAthleteSports(@PathParam("username") String username) {
        String msg;
        try {
            Athlete athlete = athleteBean.find(username);
            if (athlete != null) {
                GenericEntity<List<SportDTO>> entity
                        = new GenericEntity<List<SportDTO>>(SportController.toDTOs(athlete.getSports())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_ATHLETE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_ATHLETE_SPORTS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
