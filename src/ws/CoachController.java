package ws;

import dtos.CoachDTO;
import dtos.SportDTO;
import ejbs.CoachBean;
import entities.Coach;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/coaches")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class CoachController {
    @EJB
    private CoachBean coachBean;

    public static List<CoachDTO> toDTOs(Set<Coach> coaches) {
        return coaches.stream().map(CoachController::toDTO).collect(Collectors.toList());
    }

    // Converts an entity Coach to a DTO Coach class
    public static CoachDTO toDTO(Coach coach){
        CoachDTO coachDTO = new CoachDTO(
                coach.getUsername(),
                coach.getPassword(),
                coach.getName(),
                coach.getEmail()
        );

        coachDTO.setSports(SportController.toDTOs(coach.getSports()));
        return coachDTO;
    }

    // Converts an entity Coach to a DTO Coach class
    private CoachDTO toDTONoSports(Coach coach){
        return new CoachDTO(
                coach.getUsername(),
                coach.getPassword(),
                coach.getName(),
                coach.getEmail()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<CoachDTO> toDTOsNoSports(List<Coach> coaches){
        return coaches.stream().map(this::toDTONoSports).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/coaches/”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOsNoSports(coachBean.all())).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_COACHES", e);
        }
    }

    @GET
    @Path("{username}")
    public Response getCoachDetails(@PathParam("username") String username) {
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

    @POST
    @Path("/")
    public Response createNewCoach (CoachDTO coachDTO) throws MyEntityExistsException {
        Coach coach = coachBean.create(coachDTO.getUsername(),
                coachDTO.getPassword(),
                coachDTO.getName(),
                coachDTO.getEmail());
        return Response.status(Response.Status.CREATED).entity(toDTO(coach)).build();
    }

    @PUT
    @Path("{username}")
    public Response updateCoach(@PathParam("username") String username, CoachDTO coachDTO) throws MyEntityNotFoundException {
        Coach coach = coachBean.update(username,
                coachDTO.getPassword(),
                coachDTO.getName(),
                coachDTO.getEmail());
        return Response.status(Response.Status.OK).entity(toDTO(coach)).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteCoach (@PathParam("username") String username) throws MyEntityNotFoundException{
        coachBean.delete(username);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{username}/sports")
    public Response getCoachSports(@PathParam("username") String username) {
        String msg;
        try {
            Coach coach = coachBean.find(username);
            if (coach != null) {
                GenericEntity<List<SportDTO>> entity
                        = new GenericEntity<List<SportDTO>>(SportController.toDTOs(coach.getSports())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_COACH";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_COACH_SPORTS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
