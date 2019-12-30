package ejbs;

import dtos.LineItemOrderDTO;
import dtos.OrderDTO;
import dtos.PaymentDTO;
import entities.LineItemOrder;
import entities.Order;
import entities.Payment;
import entities.User;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class OrderBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    LineItemOrderBean lineItemOrderBean;

    @EJB
    PaymentBean paymentBean;

    public Order create(User user) {
        try {
            Order order = new Order(user);

            em.persist(order);

            return order;
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_ORDER", e);
        }
    }

    public List<OrderDTO> all() {
        try{
            List<Order> orders = (List<Order>) em.createNamedQuery("getAllOrders").getResultList();
            List<OrderDTO> orderDTOs = new ArrayList<>();

            for (Order order:orders) {
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

                orderDTOs.add(orderDTO);
            }

            return orderDTOs;
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ORDERS", e);
        }
    }

    public List<OrderDTO> ordersFromUser(String username) {
        try {
            List<Order> orders = (List<Order>) em.createNamedQuery("getAllOrders").getResultList();
            List<OrderDTO> orderDTOs = new ArrayList<>();

            for (Order order:orders) {
                if(order.getUser().getUsername().equals(username)) {
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

                    orderDTOs.add(orderDTO);
                }
            }

            return orderDTOs;
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ORDERS_FROM_USER", e);
        }
    }

    public Order find(int id) {
        try{
            return em.find(Order.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_ORDER", e);
        }
    }

    public void setLineItemInOrder(int lineItemID, int orderID) {
        try {
            Order order = find(orderID);
            LineItemOrder lineItemOrder = lineItemOrderBean.find(lineItemID);

            order.addItem(lineItemOrder);
        } catch (Exception e) {
            throw new EJBException("ERROR_ADDING_ITEM_IN_ORDER", e);
        }
    }

    public void unsetLineItemInOrder(int lineItemID, int orderID) {
        try {
            Order order = find(orderID);
            LineItemOrder lineItemOrder = lineItemOrderBean.find(lineItemID);

            order.removeItem(lineItemOrder);
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVING_ITEM_IN_ORDER", e);
        }
    }

    public void setPaymentInOrder(int paymentID, int orderID) {
        try {
            Order order = find(orderID);
            Payment payment = paymentBean.find(paymentID);

            order.addPayment(payment);
        } catch (Exception e) {
            throw new EJBException("ERROR_ADDING_PAYMENT_IN_ORDER", e);
        }
    }

    public void unsetPaymentInOrder(int paymentID, int orderID) {
        try {
            Order order = find(orderID);
            Payment payment = paymentBean.find(paymentID);

            order.removePayment(payment);
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVING_PAYMENT_IN_ORDER", e);
        }
    }

    public Order updateTotalPrice(int id) {
        try {
            Order order = find(id);
            double total = 0;
            for (LineItemOrder item: order.getLineItemOrders()) {
                total += item.getPriceQuantity();
            }
            order.setPriceTotal(total);
            em.persist(order);

            return order;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_TOTAL_IN_ORDER", e);
        }
    }

    public Order updatePayed(int id) {
        try {
            Order order = find(id);
            double total = 0;
            for (Payment item: order.getPayments()) {
                total += item.getAmount();
            }
            order.setMissing(order.getPriceTotal() - total);

            if(order.getMissing() == 0) {
                order.setStatus("PAGO");
            } else if(order.getMissing() == order.getPriceTotal()) {
                order.setStatus("NÃO PAGO");
            } else if(order.getMissing() > 0 && order.getMissing() < order.getPriceTotal()) {
                order.setStatus("PARCIAL");
            } else {
                order.setStatus("INVALID");
            }

            em.persist(order);

            return order;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_CREDIT_IN_ORDER", e);
        }
    }
}
