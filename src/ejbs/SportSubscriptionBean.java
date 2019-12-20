package ejbs;

import entities.ActiveSport;
import entities.Athlete;
import entities.SportSubscription;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "SportSubscriptionEJB")
public class SportSubscriptionBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private ActiveSportBean activeSportBean;
    @EJB
    private AthleteBean athleteBean;

    public SportSubscription create (String name, int activeSportCode, String athleteUsername) throws MyEntityExistsException, MyEntityNotFoundException {
        ActiveSport activeSport = activeSportBean.find(activeSportCode);
        if(activeSport == null){
            throw new MyEntityNotFoundException("Active Sport not found");
        }
        Athlete athlete = athleteBean.find(athleteUsername);
        if(athlete == null){
            throw new MyEntityNotFoundException("Athlete not found");
        }
        Long count = (Long) em.createNamedQuery("countSportSubscriptionByActiveSportAndAthlete").setParameter("activeSport", activeSport).setParameter("athlete", athlete).getSingleResult();
        if(count != 0){
            throw new MyEntityExistsException(athlete.getName() + " has already signed up for " + activeSport.getSport().getName() + " in the " + activeSport.getSeason().getName() +  " season");
        }
        try {
            SportSubscription sportSubscription = new SportSubscription(name, activeSport, athlete);
            em.persist(sportSubscription);
            athlete.addSportSubscription(sportSubscription);
            return sportSubscription;
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_SPORT_SUBSCRIPTION", e);
        }
    }

    public List<SportSubscription> all() {
        try {
            return (List<SportSubscription>) em.createNamedQuery("getAllSportSubscriptions").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SPORT_SUBSCRIPTIONS", e);
        }
    }

    public SportSubscription find(int code) {
        try {
            return em.find(SportSubscription.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_SPORT_SUBSCRIPTION",e);
        }
    }

    public SportSubscription update(int code, String name, int activeSportCode, String athleteUsername) throws MyEntityNotFoundException, MyEntityExistsException {
        SportSubscription sportSubscription = find(code);
        if(sportSubscription == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_SPORT_SUBSCRIPTION");
        }
        ActiveSport activeSport = activeSportBean.find(activeSportCode);
        if(activeSport == null){
            throw new MyEntityNotFoundException("Active Sport not found");
        }
        Athlete athlete = athleteBean.find(athleteUsername);
        if(athlete == null){
            throw new MyEntityNotFoundException("Athlete not found");
        }
        Long count = (Long) em.createNamedQuery("countSportSubscriptionByActiveSportAndAthlete").setParameter("activeSport", activeSport).setParameter("athlete", athlete).getSingleResult();
        if(count != 0 && (activeSportCode != sportSubscription.getActiveSport().getCode() || !athleteUsername.equals(sportSubscription.getAthlete().getUsername()))){
            throw new MyEntityExistsException(athlete.getName() + " has already signed up for " + activeSport.getSport().getName() + " in the " + activeSport.getSeason().getName() +  " season");
        }
        try{
            em.lock(sportSubscription, LockModeType.OPTIMISTIC);
            if(!athlete.getSportSubscriptions().contains(sportSubscription)){
                sportSubscription.getAthlete().removeSportSubscription(sportSubscription);
                athlete.addSportSubscription(sportSubscription);
                sportSubscription.setAthlete(athlete);
            }
            sportSubscription.setActiveSport(activeSport);
            sportSubscription.setName(name);
            em.merge(sportSubscription);
            return sportSubscription;
        }catch (Exception e){
            throw new EJBException("ERROR_UPDATING_SPORT_SUBSCRIPTION", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        SportSubscription sportSubscription = find(code);
        if(sportSubscription == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_SPORT_SUBSCRIPTION");
        }
        Athlete athlete = sportSubscription.getAthlete();
        if(athlete != null){
            athlete.removeSportSubscription(sportSubscription);
        }
        try{
            em.lock(sportSubscription, LockModeType.OPTIMISTIC);
            em.remove(sportSubscription);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_SPORT_SUBSCRIPTION", e);
        }
    }

}
