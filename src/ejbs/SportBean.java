package ejbs;

import entities.Coach;
import entities.Sport;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "SportEJB")
public class SportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private CoachBean coachBean;

    public Sport create (int code, String name) throws MyEntityExistsException {
        if (find(code)!=null){
            throw new MyEntityExistsException("Code '" + code + "' already exists");
        }
        try {
            Sport sport = new Sport(code, name);
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

    public Sport update(int code, String name) throws MyEntityNotFoundException {
        Sport sport = find(code);
        if(sport == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_SPORT");
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

    public void associate(int sportCode, String coachUsername){
        try{
            Sport sport = find(sportCode);
            Coach coach = coachBean.find(coachUsername);
            sport.addCoach(coach);
            coach.addSport(sport);
        } catch (Exception e){
            throw new EJBException("ERROR_ASSOCIATE_COACH", e);
        }
    }

    public void dissociate(int sportCode, String coachUsername){
        try{
            Sport sport = find(sportCode);
            Coach coach = coachBean.find(coachUsername);
            sport.removeCoach(coach);
            coach.removeSport(sport);
        } catch (Exception e){
            throw new EJBException("ERROR_DISSOCIATE_COACH", e);
        }
    }
}
