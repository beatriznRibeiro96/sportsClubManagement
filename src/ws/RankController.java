package ws;

import dtos.RankDTO;
import ejbs.RankBean;
import entities.Rank;
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

@Path("/ranks") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class RankController {
    @EJB
    private RankBean rankBean;

    public static RankDTO toDTO(Rank rank){
        return new RankDTO(
                rank.getCode(),
                rank.getName(),
                rank.getIdadeMin(),
                rank.getIdadeMax(),
                rank.getActiveSport().getCode(),
                rank.getActiveSport().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<RankDTO> toDTOs(Collection<Rank> ranks){
        return ranks.stream().map(RankController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/ranks/”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOs(rankBean.all())).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_RANKS", e);
        }
    }

    @GET
    @Path("{code}")
    public Response getRankDetails(@PathParam("code") int code) {
        String msg;
        try {
            Rank rank = rankBean.find(code);
            if (rank != null) {
                return Response.status(Response.Status.OK)
                        .entity(toDTO(rank))
                        .build();
            }
            msg = "ERROR_FINDING_RANK";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_RANK_DETAILS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @POST
    @Path("/")
    public Response createNewRank (RankDTO rankDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        Rank rank = rankBean.create(rankDTO.getName(),
                rankDTO.getIdadeMin(),
                rankDTO.getIdadeMax(),
                rankDTO.getActiveSportCode());
        return Response.status(Response.Status.CREATED).entity(toDTO(rank)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateRank(@PathParam("code") int code, RankDTO rankDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        Rank rank = rankBean.update(code,
                rankDTO.getName(),
                rankDTO.getIdadeMin(),
                rankDTO.getIdadeMax(),
                rankDTO.getActiveSportCode());
        return Response.status(Response.Status.OK).entity(toDTO(rank)).build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteRank (@PathParam("code") int code) throws MyEntityNotFoundException{
        rankBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
}
