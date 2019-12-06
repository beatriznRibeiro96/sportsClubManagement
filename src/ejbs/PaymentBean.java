package ejbs;

import entities.MethodPayment;
import entities.Order;
import entities.Payment;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class PaymentBean {

    @PersistenceContext
    EntityManager em;

    public Payment create(double amount, Order order, MethodPayment methodPayment) {
        try {
            Payment payment = new Payment(amount, order, methodPayment);

            em.persist(payment);

            return payment;
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_PAYMENT", e);
        }
    }

    public Payment find(int id) {
        try{
            return em.find(Payment.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PAYMENT", e);
        }
    }
}
