package ejbs;

import entities.Coach;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;


import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "CoachEJB")
public class CoachBean {
    @PersistenceContext
    private EntityManager em;

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
            throw new EJBException("ERROR_UPDATING_COAC", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Coach coach = find(username);
        if(coach == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_COACH");
        }
        try{
            em.lock(coach, LockModeType.OPTIMISTIC);
            em.remove(coach);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_COACH", e);
        }
    }
}
