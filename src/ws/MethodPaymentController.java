package ws;

import dtos.MethodPaymentDTO;
import ejbs.MethodPaymentBean;
import entities.MethodPayment;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/methodpayments")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class MethodPaymentController {

    @EJB
    MethodPaymentBean methodPaymentBean;

    MethodPaymentDTO toDTO(MethodPayment methodPayment) {
        return new MethodPaymentDTO(
                methodPayment.getId(),
                methodPayment.getMethod(),
                methodPayment.isInvalid()
        );
    }

    List<MethodPaymentDTO> toDTOs(List<MethodPayment> categories) {
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<MethodPaymentDTO> all() {
        try {
            return toDTOs(methodPaymentBean.all());
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_METHOD_PAYMENTS", e);
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            MethodPayment methodPayment = methodPaymentBean.find(id);

            if(methodPayment != null) {
                return Response.status(Response.Status.OK).entity(toDTO(methodPayment)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("/")
    public Response createNewProduct(MethodPaymentDTO methodPaymentDTO) {
        try {
            MethodPayment newMethod = methodPaymentBean.create(methodPaymentDTO.getMethod());

            if(newMethod != null) {
                return Response.status(Response.Status.OK).entity(toDTO(newMethod)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int idFromMethodToUpdate, MethodPaymentDTO methodPaymentDTO) {
        try {
            MethodPayment methodPayment = methodPaymentBean.update(idFromMethodToUpdate, methodPaymentDTO.getMethod());

            if(methodPayment != null) {
                return Response.status(Response.Status.OK).entity(toDTO(methodPayment)).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        try {
            MethodPayment methodPayment = methodPaymentBean.delete(id);

            if(methodPayment != null) {
                return Response.status(Response.Status.OK).entity(toDTO(methodPayment)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }
}
