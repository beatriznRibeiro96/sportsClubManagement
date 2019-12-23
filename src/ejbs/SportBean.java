package ejbs;

import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "SportEJB")
public class SportBean {
    @PersistenceContext
    private EntityManager em;

    public Sport create (String name) throws MyEntityExistsException, MyConstraintViolationException {
        try {
            Long count = (Long) em.createNamedQuery("countSportByName").setParameter("name", name).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException("'" + name + "' already exists");
            }
            Sport sport = new Sport(name);
            em.persist(sport);
            return sport;
        } catch (MyEntityExistsException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_SPORT", e);
        }
    }

    public List<Sport> all() {
        try {
            return (List<Sport>) em.createNamedQuery("getAllSports").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SPORTS", e);
        }
    }

    public Sport find(int code) {
        try {
            return em.find(Sport.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_SPORT",e);
        }
    }

    public Sport update(int code, String name) throws MyEntityNotFoundException, MyEntityExistsException {
        try{
            Sport sport = find(code);
            em.lock(sport, LockModeType.OPTIMISTIC);
            if(sport == null){
                throw new MyEntityNotFoundException("Sport '" + name + "' not found.");
            }
            Long count = (Long) em.createNamedQuery("countSportByName").setParameter("name", name).getSingleResult();
            if(count != 0 && !name.equals(sport.getName())){
                throw new MyEntityExistsException("'" + name + "' already exists");
            }
            sport.setName(name);
            em.merge(sport);
            return sport;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_SPORT", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            Sport sport = find(code);
            if(sport == null){
                throw new MyEntityNotFoundException("Sport with code '" + code + "' not found.");
            }
            em.remove(sport);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_SPORT", e);
        }
    }
}
