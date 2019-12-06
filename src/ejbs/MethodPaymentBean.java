package ejbs;

import entities.MethodPayment;

import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
public class MethodPaymentBean {

    @PersistenceContext
    EntityManager em;

    public MethodPayment create(String method) {
        try {
            MethodPayment methodPayment = new MethodPayment(method);

            em.persist(methodPayment);

            return methodPayment;
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_METHOD_PAYMENT", e);
        }
    }

    public List<MethodPayment> all() {
        try{
            List<MethodPayment> methodPayments = (List<MethodPayment>) em.createNamedQuery("getAllMethods").getResultList();
            return methodPayments;
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_METHODS_PAYMENTS", e);
        }
    }

    public MethodPayment find(int id) {
        try{
            return em.find(MethodPayment.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_METHOD_PAYMENT", e);
        }
    }

    public MethodPayment update(int idMethodPaymentToUpdate, String method) {
        try{
            MethodPayment methodPayment = find(idMethodPaymentToUpdate);

            methodPayment.setMethod(method);

            em.persist(methodPayment);

            return methodPayment;
        }catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_METHOD_PAYMENT", e);
        }
    }

    public MethodPayment delete(int id) {
        try{
            MethodPayment methodPayment = find(id);

            methodPayment.setInvalid(true);

            em.persist(methodPayment);

            return methodPayment;
        }catch (Exception e) {
            throw new EJBException("ERROR_DELETING_METHOD_PAYMENT", e);
        }
    }
}
