package ejbs;

import entities.Administrator;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "AdministratorEJB")
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;

    public Administrator create(String username, String password, String name, String email) throws MyEntityExistsException {
        if(find(username) != null){
            throw new MyEntityExistsException("Username '" + username + "' already taken");
        }
        try{
            Administrator administrator = new Administrator(username, password, name, email);
            em.persist(administrator);
            return administrator;
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_ADMINISTRATOR", e);
        }
    }

    public List<Administrator> all() {
        try {
            return (List<Administrator>) em.createNamedQuery("getAllAdministrators").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ADMINISTRATORS", e);
        }
    }

    public Administrator find(String username) {
        try {
            return em.find(Administrator.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_ADMINSITRATOR",e);
        }
    }

    public Administrator update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        Administrator administrator = find(username);
        if(administrator == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_ADMINISTRATOR");
        }
        try {
            em.lock(administrator, LockModeType.OPTIMISTIC);
            administrator.setPassword(password);
            administrator.setName(name);
            administrator.setEmail(email);
            em.merge(administrator);
            return administrator;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_ADMINISTRATOR", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Administrator administrator = find(username);
        if(administrator == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_ADMINISTRATOR");
        }
        try {
            em.lock(administrator, LockModeType.OPTIMISTIC);
            em.remove(administrator);
        }catch (Exception e){
            e.getMessage();
        }
    }
}
