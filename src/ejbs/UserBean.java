package ejbs;

import entities.Order;
import entities.User;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class UserBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    private OrderBean orderBean;

    public User find(String id) {
        try{
            return em.find(User.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_USER", e);
        }
    }

    public void setOrderInUser(int orderID, String userID) {
        try {
            Order order = orderBean.find(orderID);
            User user = find(userID);

            user.addOrder(order);
        } catch (Exception e) {
            throw new EJBException("ERROR_ADDING_ORDER_IN_USER", e);
        }
    }

    public void unsetOrderInUser(int orderID, String userID) {
        try {
            Order order = orderBean.find(orderID);
            User user = find(userID);

            user.removeOrder(order);
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVING_ORDER_IN_USER", e);
        }
    }

    public User authenticate(final String username, final String password) throws
            Exception {
        User user = find(username);
        if (user != null &&
                user.getPassword().equals(User.hashPassword(password))) {
            return user;
        }
        throw new Exception("Failed logging in with username '" + username + "': unknown username or wrong password");
    }
}
