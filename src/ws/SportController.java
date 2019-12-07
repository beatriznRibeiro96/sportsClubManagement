package ws;

import dtos.AthleteDTO;
import dtos.CoachDTO;
import dtos.RankDTO;
import dtos.SportDTO;
import ejbs.SportBean;
import entities.Sport;
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
import java.util.stream.Collectors;

@Path("/sports") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class SportController {
    @EJB
    private SportBean sportBean;

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
    @Path("/") // means: the relative url path is “/api/students/”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOs(sportBean.all())).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_SPORTS", e);
        }
    }

    @GET
    @Path("{code}")
    public Response getSportDetails(@PathParam("code") int code) {
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

    @POST
    @Path("/")
    public Response createNewSport (SportDTO sportDTO) throws MyEntityExistsException {
        Sport sport = sportBean.create(sportDTO.getCode(),
                sportDTO.getName());
        return Response.status(Response.Status.CREATED).entity(toDTO(sport)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateSport(@PathParam("code") int code, SportDTO sportDTO) throws MyEntityNotFoundException {
        Sport sport = sportBean.update(code,
                sportDTO.getName());
        return Response.status(Response.Status.OK).entity(toDTO(sport)).build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteSport (@PathParam("code") int code) throws MyEntityNotFoundException{
        sportBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{code}/coaches")
    public Response getSportCoaches(@PathParam("code") int code) {
        String msg;
        try {
            Sport sport = sportBean.find(code);
            if (sport != null) {
                GenericEntity<List<CoachDTO>> entity
                        = new GenericEntity<List<CoachDTO>>(CoachController.toDTOs(sport.getCoaches())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_SPORT_COACHES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/athletes")
    public Response getSportAthletes(@PathParam("code") int code) {
        String msg;
        try {
            Sport sport = sportBean.find(code);
            if (sport != null) {
                GenericEntity<List<AthleteDTO>> entity
                        = new GenericEntity<List<AthleteDTO>>(AthleteController.toDTOs(sport.getAthletes())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_SPORT_ATHLETES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/ranks")
    public Response getSportRanks(@PathParam("code") int code) {
        String msg;
        try {
            Sport sport = sportBean.find(code);
            if (sport != null) {
                GenericEntity<List<RankDTO>> entity
                        = new GenericEntity<List<RankDTO>>(RankController.toDTOs(sport.getRanks())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_SPORT";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_SPORT_RANKS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
