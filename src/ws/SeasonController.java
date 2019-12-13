package ws;

import dtos.SeasonDTO;
import ejbs.SeasonBean;
import entities.Season;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/seasons") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class SeasonController {
    @EJB
    private SeasonBean seasonBean;

    public static SeasonDTO toDTO(Season season){
        return new SeasonDTO(
                season.getCode(),
                season.getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<SeasonDTO> toDTOs(Collection<Season> seasons){
        return seasons.stream().map(SeasonController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/seasons/”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOs(seasonBean.all())).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_SEASONS", e);
        }
    }

    @GET
    @Path("{code}")
    public Response getSeasonDetails(@PathParam("code") int code) {
        String msg;
        try {
            Season season = seasonBean.find(code);
            if (season != null) {
                return Response.status(Response.Status.OK)
                        .entity(toDTO(season))
                        .build();
            }
            msg = "ERROR_FINDING_SEASON";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_SEASON_DETAILS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @POST
    @Path("/")
    public Response createNewSeason (SeasonDTO seasonDTO) throws MyEntityExistsException {
        Season season = seasonBean.create(seasonDTO.getCode(),
                seasonDTO.getName());
        return Response.status(Response.Status.CREATED).entity(toDTO(season)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateSeason(@PathParam("code") int code, SeasonDTO seasonDTO) throws MyEntityNotFoundException {
        Season season = seasonBean.update(code,
                seasonDTO.getName());
        return Response.status(Response.Status.OK).entity(toDTO(season)).build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteSeason (@PathParam("code") int code) throws MyEntityNotFoundException{
        seasonBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
}
