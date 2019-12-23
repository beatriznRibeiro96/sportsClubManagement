package ws;

import dtos.PartnerDTO;
import ejbs.PartnerBean;
import entities.Partner;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyParseDateException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Path("/partners")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class PartnerController {
    @EJB
    private PartnerBean partnerBean;

    // Converts an entity Partner to a DTO Partner class
    private PartnerDTO toDTO(Partner partner){
        return new PartnerDTO(
                partner.getUsername(),
                partner.getPassword(),
                partner.getName(),
                partner.getEmail(),
                partner.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<PartnerDTO> toDTOs(List<Partner> partners){
        return partners.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/partners/”
    public Response all() {
        return Response.status(200).entity(toDTOs(partnerBean.all())).build();
    }

    @GET
    @Path("{username}")
    public Response getPartnerDetails(@PathParam("username") String username) {
        String msg;
        try {
            Partner partner = partnerBean.find(username);
            if (partner != null) {
                return Response.status(Response.Status.OK)
                        .entity(toDTO(partner))
                        .build();
            }
            msg = "ERROR_FINDING_PARTNER";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_PARTNER_DETAILS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @POST
    @Path("/")
    public Response createNewPartner (PartnerDTO partnerDTO) throws MyEntityExistsException, MyConstraintViolationException, MyParseDateException {
        Partner partner = partnerBean.create(partnerDTO.getUsername(),
                partnerDTO.getPassword(),
                partnerDTO.getName(),
                partnerDTO.getEmail(),
                partnerDTO.getBirthDate());
        return Response.status(Response.Status.CREATED).entity(toDTO(partner)).build();
    }

    @PUT
    @Path("{username}")
    public Response updatePartner(@PathParam("username") String username, PartnerDTO partnerDTO) throws MyEntityNotFoundException, MyParseDateException {
        Partner partner = partnerBean.update(username,
                partnerDTO.getPassword(),
                partnerDTO.getName(),
                partnerDTO.getEmail(),
                partnerDTO.getBirthDate());
        return Response.status(Response.Status.OK).entity(toDTO(partner)).build();
    }

    @DELETE
    @Path("{username}")
    public Response deletePartner (@PathParam("username") String username) throws MyEntityNotFoundException{
        partnerBean.delete(username);
        return Response.status(Response.Status.OK).build();
    }
}
