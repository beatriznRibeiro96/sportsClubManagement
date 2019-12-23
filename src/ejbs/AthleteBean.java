package ejbs;

import entities.Athlete;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless (name = "AthleteEJB")
public class AthleteBean {
    @PersistenceContext
    private EntityManager em;

    public Athlete create(String username, String password, String name, String email) throws MyEntityExistsException, MyConstraintViolationException {
        try{
            if(find(username) != null){
                throw new MyEntityExistsException("Username '" + username + "' already exists");
            }
            Athlete athlete = new Athlete(username, password, name, email);
            em.persist(athlete);
            return athlete;
        } catch(MyEntityExistsException e){
            throw e;
        } catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_ATHLETE", e);
        }
    }
    public List<Athlete> all() {
        try {
            return (List<Athlete>) em.createNamedQuery("getAllAthletes").getResultList();
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
        try {
            Athlete athlete = find(username);
            em.lock(athlete, LockModeType.OPTIMISTIC);
            if(athlete == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            athlete.setPassword(password);
            athlete.setName(name);
            athlete.setEmail(email);
            em.merge(athlete);
            return athlete;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_ATHELETE", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        try {
            Athlete athlete = find(username);
            if(athlete == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            em.remove(athlete);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            e.getMessage();
        }
    }
}
