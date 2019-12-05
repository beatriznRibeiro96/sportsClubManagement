package ejbs;

import entities.Partner;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "PartnerEJB")
public class PartnerBean {
    @PersistenceContext
    private EntityManager em;

    public Partner create (String username,String password, String name, String email) throws MyEntityExistsException {
        if (find(username)!=null){
            throw new MyEntityExistsException("Username '" + username + "' already exists");
        }
        try {
            Partner partner = new Partner(username, password, name, email);
            em.persist(partner);
            return partner;
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
        Partner partner = find(username);
        if(partner == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_PARTNER");
        }
        try{
            em.lock(partner, LockModeType.OPTIMISTIC);
            partner.setName(name);
            partner.setEmail(email);
            partner.setPassword(password);
            em.merge(partner);
            return partner;
        }catch (Exception e){
            throw new EJBException("ERROR_UPDATING_PARTNER", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Partner partner = find(username);
        if(partner == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_PARTNER");
        }
        try{
            em.lock(partner, LockModeType.OPTIMISTIC);
            em.remove(partner);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_PARTNER", e);
        }
    }
}
