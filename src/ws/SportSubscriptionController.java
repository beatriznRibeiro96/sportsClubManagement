package ws;

import dtos.SportSubscriptionDTO;
import ejbs.SportSubscriptionBean;
import entities.SportSubscription;
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

@Path("/sportSubscriptions") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class SportSubscriptionController {
    @EJB
    private SportSubscriptionBean sportSubscriptionBean;

    public static SportSubscriptionDTO toDTO(SportSubscription sportSubscription){
        return new SportSubscriptionDTO(
                sportSubscription.getCode(),
                sportSubscription.getName(),
                sportSubscription.getActiveSport().getCode(),
                sportSubscription.getActiveSport().getName(),
                sportSubscription.getAthlete().getUsername(),
                sportSubscription.getAthlete().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<SportSubscriptionDTO> toDTOs(Collection<SportSubscription> sportSubscriptions){
        return sportSubscriptions.stream().map(SportSubscriptionController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/sportSubscriptions/”
    public Response all() {
        try {
            return Response.status(200).entity(toDTOs(sportSubscriptionBean.all())).build();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_SPORT_SUBSCRIPTIONS", e);
        }
    }

    @GET
    @Path("{code}")
    public Response getSportSubscriptionDetails(@PathParam("code") int code) {
        String msg;
        try {
            SportSubscription sportSubscription = sportSubscriptionBean.find(code);
            if (sportSubscription != null) {
                return Response.status(Response.Status.OK)
                        .entity(toDTO(sportSubscription))
                        .build();
            }
            msg = "ERROR_FINDING_SPORT_SUBSCRIPTION";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_SPORT_SUBSCRIPTION_DETAILS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @POST
    @Path("/")
    public Response createNewSportSubscription (SportSubscriptionDTO sportSubscriptionDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        SportSubscription sportSubscription = sportSubscriptionBean.create(sportSubscriptionDTO.getName(),
                sportSubscriptionDTO.getActiveSportCode(),
                sportSubscriptionDTO.getAthleteUsername());
        return Response.status(Response.Status.CREATED).entity(toDTO(sportSubscription)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateSportSubscription(@PathParam("code") int code, SportSubscriptionDTO sportSubscriptionDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        SportSubscription sportSubscription = sportSubscriptionBean.update(code,
                sportSubscriptionDTO.getName(),
                sportSubscriptionDTO.getActiveSportCode(),
                sportSubscriptionDTO.getAthleteUsername());
        return Response.status(Response.Status.OK).entity(toDTO(sportSubscription)).build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteSportSubscription (@PathParam("code") int code) throws MyEntityNotFoundException{
        sportSubscriptionBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
}
