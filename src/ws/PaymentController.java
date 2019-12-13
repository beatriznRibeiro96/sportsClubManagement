package ws;

import dtos.PaymentDTO;
import ejbs.MethodPaymentBean;
import ejbs.OrderBean;
import ejbs.PaymentBean;
import entities.MethodPayment;
import entities.Order;
import entities.Payment;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payments")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PaymentController {

    @EJB
    private OrderBean orderBean;

    @EJB
    private PaymentBean paymentBean;

    @EJB
    private MethodPaymentBean methodPaymentBean;

    PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getOrder().getId(),
                payment.getMethodPayment().getId()
        );
    }

    @POST
    @Path("/")
    public Response createNewPayment(PaymentDTO paymentDTO) {
        try {
            Order order = orderBean.find(paymentDTO.getOrderID());
            MethodPayment methodPayment = methodPaymentBean.find(paymentDTO.getMethodPaymentID());
            Payment payment = paymentBean.create(paymentDTO.getAmount(), order, methodPayment);
            orderBean.setPaymentInOrder(payment.getId(), order.getId());
            orderBean.updatePayed(order.getId());

            Order updatedOrder = orderBean.find(order.getId());
            if(updatedOrder != null) {
                return Response.status(Response.Status.OK).entity(toDTO(payment)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

}
