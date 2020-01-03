package ejbs;

import dtos.UserDTO;
import entities.Message;
import entities.User;
import exceptions.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Stateless(name = "MessageEJB")
public class MessageBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserBean userBean;

    public Message create (String subject, String body) throws MyConstraintViolationException {
        try {
            Message message = new Message(subject, body);
            em.persist(message);
            return message;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_MESSAGE", e);
        }
    }

    public List<Message> all() {
        try {
            return (List<Message>) em.createNamedQuery("getAllMessages").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_MESSAGES", e);
        }
    }

    public Message find(int code) {
        try {
            return em.find(Message.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_MESSAGE",e);
        }
    }

    public Message update(int code, String subject, String body) throws MyEntityNotFoundException, MyEntityExistsException {
        try{
            Message message = find(code);
            em.lock(message, LockModeType.OPTIMISTIC);
            if(message == null){
                throw new MyEntityNotFoundException("Message with code '" + code + "' not found.");
            }
            message.setSubject(subject);
            message.setBody(body);
            em.merge(message);
            return message;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_MESSAGE", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            Message message = find(code);
            if(message == null){
                throw new MyEntityNotFoundException("Message with code '" + code + "' not found.");
            }
            Set<User> users = message.getUsers();
            if(users != null){
                for (User user:users) {
                    user.removeMessage(message);
                }
            }
            em.remove(message);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_MESSAGE", e);
        }
    }

    public void addMessageToUser(int messageCode, String userUsername) throws MyEntityNotFoundException {
        try {
            Message message = find(messageCode);
            if(message == null){
                throw new MyEntityNotFoundException("Message with code '" + messageCode + "' not found.");
            }
            User user = userBean.find(userUsername);
            if (user == null) {
                throw new MyEntityNotFoundException("User with username '" + userUsername + "' not found.");
            }
            message.addUser(user);
            user.addMessage(message);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_ADDING_MESSAGE_TO_USER ---->" + e.getMessage());
        }
    }
}
