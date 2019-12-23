package ejbs;

import entities.Administrator;
import exceptions.*;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless(name = "AdministratorEJB")
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;

    public Administrator create(String username, String password, String name, String email, String birthDate) throws MyEntityExistsException, MyConstraintViolationException, MyParseDateException {
        try{
            if(find(username) != null){
                throw new MyEntityExistsException("Username: '" + username + "' already exists");
            }
            if (birthDate.isEmpty()){
                throw new MyParseDateException("Birth date cannot be empty");
            }
            LocalDate dataNascimento = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Administrator administrator = new Administrator(username, password, name, email, dataNascimento);
            em.persist(administrator);
            return administrator;
        } catch(MyParseDateException | MyEntityExistsException e){
            throw e;
        } catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
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

    public Administrator update(String username, String password, String name, String email, String birthDate) throws MyEntityNotFoundException, MyParseDateException {
        try {
            Administrator administrator = find(username);
            em.lock(administrator, LockModeType.OPTIMISTIC);
            if(administrator == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            if (birthDate.isEmpty()){
                throw new MyParseDateException("Birth date cannot be empty");
            }
            LocalDate dataNascimento = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            administrator.setPassword(password);
            administrator.setName(name);
            administrator.setEmail(email);
            administrator.setBirthDate(dataNascimento);
            em.merge(administrator);
            return administrator;
        } catch (MyEntityNotFoundException | MyParseDateException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_ADMINISTRATOR", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        try {
            Administrator administrator = find(username);
            if(administrator == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            em.remove(administrator);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            e.getMessage();
        }
    }
}
