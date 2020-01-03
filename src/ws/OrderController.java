package ws;

import dtos.LineItemOrderDTO;
import dtos.OrderDTO;
import dtos.PaymentDTO;
import ejbs.*;
import entities.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderController {

    @EJB
    private OrderBean orderBean;

    @EJB
    private LineItemOrderBean lineItemOrderBean;

    @EJB
    private ProductBean productBean;

    @EJB
    private PaymentBean paymentBean;

    @EJB
    private MethodPaymentBean methodPaymentBean;

    @EJB
    private UserBean userBean;

    OrderDTO toDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO(
                order.getId(),
                order.getPriceTotal(),
                order.getUser().getUsername(),
                order.getStatus(),
                order.getMissing(),
                order.isInvalid()
        );

        for (LineItemOrder item : order.getLineItemOrders()) {
            orderDTO.getLineItemOrders().add(new LineItemOrderDTO(
                    item.getId(),
                    item.getProduct().getId(),
                    item.getProduct().getDescription(),
                    item.getOrder().getId(),
                    item.getQuantity(),
                    item.getPriceQuantity()
            ));
        }

        for (Payment payment : order.getPayments()) {
            orderDTO.getPayments().add(new PaymentDTO(
                    payment.getId(),
                    payment.getAmount(),
                    payment.getOrder().getId(),
                    payment.getMethodPayment().getId()
            ));
        }

        return orderDTO;
    }

    List<OrderDTO> toDTOs(List<Order> categories) {
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<OrderDTO> all() {
        try {
            return orderBean.all();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_ORDERS", e);
        }
    }

    @GET
    @Path("/{username}")
    public List<OrderDTO> all(@PathParam("username") String username) {
        try {
            return orderBean.ordersFromUser(username);
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_ORDERS", e);
        }
    }

    @GET
    @Path("/{username}/{id}")
    public Response getById(@PathParam("username") String username, @PathParam("id") int id) {
        try {
            Order order = orderBean.find(id);

            if(order != null) {
                return Response.status(Response.Status.OK).entity(toDTO(order)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("/")
    public Response createNewOrder(OrderDTO orderDTO) {
        try {
            User user = userBean.find(orderDTO.getUsername());
            Order order = orderBean.create(user);
            for (LineItemOrderDTO item : orderDTO.getLineItemOrders()) {
                Product product = productBean.find(item.getProductID());
                LineItemOrder lineItemOrder = lineItemOrderBean.create(product, order, item.getQuantity());
                orderBean.setLineItemInOrder(lineItemOrder.getId(), order.getId());
            }
            orderBean.updateTotalPrice(order.getId());

            for (PaymentDTO item: orderDTO.getPayments()) {
                MethodPayment methodPayment = methodPaymentBean.find(item.getMethodPaymentID());
                Payment payment = paymentBean.create(item.getAmount(), order, methodPayment);
                orderBean.setPaymentInOrder(payment.getId(), order.getId());
            }
            orderBean.updatePayed(order.getId());

            Order newOrder = orderBean.find(order.getId());

            if(newOrder != null) {
                return Response.status(Response.Status.OK).entity(toDTO(newOrder)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

}
