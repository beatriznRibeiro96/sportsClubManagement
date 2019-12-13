package ejbs;

import entities.LineItemOrder;
import entities.Order;
import entities.Product;

import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
public class LineItemOrderBean {

    @PersistenceContext
    EntityManager em;

    public LineItemOrder create(Product product, Order order, int quantity) {
        try {
            LineItemOrder lineItemOrder = new LineItemOrder(product, order, quantity);

            em.persist(lineItemOrder);

            return lineItemOrder;
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_LINE_ITEM_ORDER", e);
        }
    }

    public List<LineItemOrder> all() {
        try{
            List<LineItemOrder> categories = (List<LineItemOrder>) em.createNamedQuery("getAllItems").getResultList();
            return categories;
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_LINE_ITEM_ORDER", e);
        }
    }

    public LineItemOrder find(int id) {
        try{
            return em.find(LineItemOrder.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_LINE_ITEM_ORDER", e);
        }
    }

    public LineItemOrder update(int idFromLineItemOrderToUpdate, int quantity) {
        try{
            LineItemOrder lineItemOrder = find(idFromLineItemOrderToUpdate);
            lineItemOrder.setQuantity(quantity);
            lineItemOrder.setPriceQuantity(quantity * lineItemOrder.getProduct().getPrice());

            em.persist(lineItemOrder);

            return lineItemOrder;
        }catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_LINE_ITEM_ORDER", e);
        }
    }

    public LineItemOrder delete(int id) {
        try{
            LineItemOrder lineItemOrder = find(id);

            em.remove(lineItemOrder);

            return lineItemOrder;
        }catch (Exception e) {
            throw new EJBException("ERROR_DELETING_LINE_ITEM_ORDER", e);
        }
    }
}
