package ejbs;

import entities.Administrator;
import entities.Athlete;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless (name = "AthleteEJB")
public class AthleteBean {
    @PersistenceContext
    private EntityManager em;

    public Athlete create(String username, String password, String name, String email) throws MyEntityExistsException {
        if(find(username) != null){
            throw new MyEntityExistsException("Username '" + username + "' already taken");
        }
        try{
            Athlete athlete = new Athlete(username, password, name, email);
            em.persist(athlete);
            return athlete;
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_ATHLETE", e);
        }
    }
    public List<Athlete> all() {
        try {
            return (List<Athlete>) em.createNamedQuery("getAllAthlete").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ATHELETE", e);
        }
    }

    public Athlete find(String username) {
        try {
            return em.find(Athlete.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_ATHELETE",e);
        }
    }

    public Athlete update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        Athlete athlete = find(username);
        if(athlete == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_ATHELETE");
        }
        try {
            em.lock(athlete, LockModeType.OPTIMISTIC);
            athlete.setPassword(password);
            athlete.setName(name);
            athlete.setEmail(email);
            em.merge(athlete);
            return athlete;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_ATHELETE", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Athlete athlete = find(username);
        if(athlete == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_ATHELETE");
        }
        try {
            em.lock(athlete, LockModeType.OPTIMISTIC);
            em.remove(athlete);
        }catch (Exception e){
            e.getMessage();
        }
    }
}
