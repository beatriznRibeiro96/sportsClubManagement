package ws;

import dtos.AthleteDTO;
import dtos.CoachDTO;
import dtos.RankDTO;
import ejbs.RankBean;
import entities.Athlete;
import entities.Rank;
import entities.SportSubscription;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.stream.Collectors;

@Path("/ranks") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class RankController {
    @EJB
    private RankBean rankBean;
    @Context
    private SecurityContext securityContext;

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
        return Response.status(200).entity(toDTOs(rankBean.all())).build();
    }

    @GET
    @Path("{code}")
    public Response getRankDetails(@PathParam("code") int code) {
        if(securityContext.isUserInRole("Administrator")){
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
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @POST
    @Path("/")
    public Response createNewRank (RankDTO rankDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Rank rank = rankBean.create(rankDTO.getName(),
                rankDTO.getIdadeMin(),
                rankDTO.getIdadeMax(),
                rankDTO.getActiveSportCode());
        return Response.status(Response.Status.CREATED).entity(toDTO(rank)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateRank(@PathParam("code") int code, RankDTO rankDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        if(securityContext.isUserInRole("Administrator")) {
            Rank rank = rankBean.update(code,
                    rankDTO.getName(),
                    rankDTO.getIdadeMin(),
                    rankDTO.getIdadeMax(),
                    rankDTO.getActiveSportCode());
            return Response.status(Response.Status.OK).entity(toDTO(rank)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteRank (@PathParam("code") int code) throws MyEntityNotFoundException{
        rankBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{code}/coaches")
    public Response getRankCoaches(@PathParam("code") int code) {
        String msg;
        try {
            Rank rank = rankBean.find(code);
            if (rank != null) {
                GenericEntity<List<CoachDTO>> entity
                        = new GenericEntity<List<CoachDTO>>(CoachController.toDTOs(rank.getCoaches())) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_RANK";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_RANK_COACHES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("{code}/athletes")
    public Response getRankAthletes(@PathParam("code") int code) {
        String msg;
        try {
            Rank rank = rankBean.find(code);
            Set<Athlete> athletes = new LinkedHashSet<>();
            if (rank != null) {
                for (SportSubscription sportSubscription:rank.getSportSubscriptions()) {
                    athletes.add(sportSubscription.getAthlete());
                }
                GenericEntity<List<AthleteDTO>> entity
                        = new GenericEntity<List<AthleteDTO>>(AthleteController.toDTOs(athletes)) {
                };
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            msg = "ERROR_FINDING_RANK";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_RANK_ATHLETES --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
