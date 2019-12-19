package ejbs;

import entities.ActiveSport;
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
import java.util.Set;

@Stateless(name = "CoachEJB")
public class CoachBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private ActiveSportBean activeSportBean;

    public Coach create (String username,String password, String name, String email) throws MyEntityExistsException {
        if (find(username)!=null){
            throw new MyEntityExistsException("Username '" + username + "' already exists");
        }
        try {
            Coach coach = new Coach(username, password, name, email);
            em.persist(coach);
            return coach;
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_COACH", e);
        }
    }

    public List<Coach> all() {
        try {
            return (List<Coach>) em.createNamedQuery("getAllCoaches").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_COACHES", e);
        }
    }

    public Coach find(String username) {
        try {
            return em.find(Coach.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_COACH",e);
        }
    }

    public Coach update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        Coach coach = find(username);
        if(coach == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_COACH");
        }
        try{
            em.lock(coach, LockModeType.OPTIMISTIC);
            coach.setName(name);
            coach.setEmail(email);
            coach.setPassword(password);
            em.merge(coach);
            return coach;
        }catch (Exception e){
            throw new EJBException("ERROR_UPDATING_COACH", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Coach coach = find(username);
        if(coach == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_COACH");
        }
        Set<ActiveSport> activeSports = coach.getActiveSports();
        try{
            em.lock(coach, LockModeType.OPTIMISTIC);
            if(activeSports != null){
                for (ActiveSport activeSport:activeSports) {
                    activeSport.removeCoach(coach);
                }
            }
            em.remove(coach);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_COACH", e);
        }
    }

    public void associateCoachToActiveSport(String coachUsername, int activeSportCode){
        try{
            Coach coach = find(coachUsername);
            ActiveSport activeSport = activeSportBean.find(activeSportCode);
            coach.addActiveSport(activeSport);
            activeSport.addCoach(coach);
        } catch (Exception e){
            throw new EJBException("ERROR_ASSOCIATE_COACH_TO_ACTIVE_SPORT", e);
        }
    }

    public void dissociateCoachFromActiveSport(String coachUsername, int activeSportCode){
        try{
            Coach coach = find(coachUsername);
            ActiveSport activeSport = activeSportBean.find(activeSportCode);
            coach.removeActiveSport(activeSport);
            activeSport.removeCoach(coach);
        } catch (Exception e){
            throw new EJBException("ERROR_DISSOCIATE_COACH_FROM_ACTIVE_SPORT", e);
        }
    }
}
