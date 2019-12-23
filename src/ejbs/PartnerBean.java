package ejbs;

import entities.Partner;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "PartnerEJB")
public class PartnerBean {
    @PersistenceContext
    private EntityManager em;

    public Partner create (String username,String password, String name, String email) throws MyEntityExistsException, MyConstraintViolationException {
        try {
            if(find(username) != null){
                throw new MyEntityExistsException("Username: '" + username + "' already exists");
            }
            Partner partner = new Partner(username, password, name, email);
            em.persist(partner);
            return partner;
        } catch(MyEntityExistsException e){
            throw e;
        } catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_PARTNER", e);
        }
    }

    public List<Partner> all() {
        try {
            return (List<Partner>) em.createNamedQuery("getAllPartners").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PARTNERS", e);
        }
    }

    public Partner find(String username) {
        try {
            return em.find(Partner.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PARTNER",e);
        }
    }

    public Partner update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        try{
            Partner partner = find(username);
            em.lock(partner, LockModeType.OPTIMISTIC);
            if(partner == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            partner.setName(name);
            partner.setEmail(email);
            partner.setPassword(password);
            em.merge(partner);
            return partner;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_PARTNER", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        try{
            Partner partner = find(username);
            if(partner == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            em.remove(partner);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_PARTNER", e);
        }
    }
}
