package ejbs;

import entities.Sport;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless(name = "SportEJB")
public class SportBean {
    @PersistenceContext
    private EntityManager em;

    public Sport create (String name) throws MyEntityExistsException {
        Long count = (Long) em.createNamedQuery("countSportByName").setParameter("name", name).getSingleResult();
        if(count != 0){
            throw new MyEntityExistsException(name + "' already exists");
        }

        try {
            Sport sport = new Sport(name);
            em.persist(sport);
            return sport;
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
        Sport sport = find(code);
        if(sport == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_SPORT");
        }
        Long count = (Long) em.createNamedQuery("countSportByName").setParameter("name", name).getSingleResult();
        if(count != 0 && sport.getName() != name){
            throw new MyEntityExistsException(name + "' already exists");
        }
        try{
            em.lock(sport, LockModeType.OPTIMISTIC);
            sport.setName(name);
            em.merge(sport);
            return sport;
        }catch (Exception e){
            throw new EJBException("ERROR_UPDATING_SPORT", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        Sport sport = find(code);
        if(sport == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_SPORT");
        }
        try{
            em.lock(sport, LockModeType.OPTIMISTIC);
            em.remove(sport);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_SPORT", e);
        }
    }
}
