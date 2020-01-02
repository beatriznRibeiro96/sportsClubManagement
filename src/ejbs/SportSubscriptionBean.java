package ejbs;

import entities.ActiveSport;
import entities.Athlete;
import entities.Rank;
import entities.SportSubscription;
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

@Stateless(name = "SportSubscriptionEJB")
public class SportSubscriptionBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private RankBean rankBean;
    @EJB
    private AthleteBean athleteBean;

    public SportSubscription create (String name, int rankCode, String athleteUsername) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Rank rank = rankBean.find(rankCode);
            if(rank == null){
                throw new MyEntityNotFoundException("Rank not found.");
            }
            Athlete athlete = athleteBean.find(athleteUsername);
            if(athlete == null){
                throw new MyEntityNotFoundException("Athlete not found.");
            }
            Long count = (Long) em.createNamedQuery("countSportSubscriptionByRankAndAthlete").setParameter("rank", rank).setParameter("athlete", athlete).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException(athlete.getName() + " has already signed up for " + rank.getName() + " in the " + rank.getActiveSport().getSeason().getName() +  " season");
            }
            SportSubscription sportSubscription = new SportSubscription(name, rank, athlete);
            em.persist(sportSubscription);
            rank.addSportSubscription(sportSubscription);
            athlete.addSportSubscription(sportSubscription);
            return sportSubscription;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
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

    public SportSubscription update(int code, String name, int rankCode, String athleteUsername) throws MyEntityNotFoundException, MyEntityExistsException {
        try{
            SportSubscription sportSubscription = find(code);
            em.lock(sportSubscription, LockModeType.OPTIMISTIC);
            if(sportSubscription == null){
                throw new MyEntityNotFoundException("Sport Subscription with code '" + code + "' not found.");
            }
            Rank rank = rankBean.find(rankCode);
            if(rank == null){
                throw new MyEntityNotFoundException("Rank not found.");
            }
            Athlete athlete = athleteBean.find(athleteUsername);
            if(athlete == null){
                throw new MyEntityNotFoundException("Athlete not found.");
            }
            Long count = (Long) em.createNamedQuery("countSportSubscriptionByRankAndAthlete").setParameter("rank", rank).setParameter("athlete", athlete).getSingleResult();
            if(count != 0 && (rankCode != sportSubscription.getRank().getCode() || !athleteUsername.equals(sportSubscription.getAthlete().getUsername()))){
                throw new MyEntityExistsException(athleteUsername + " has already signed up for " + rank.getName() + " in the " + rank.getActiveSport().getSeason().getName() +  " season");
            }
            if(!athlete.getSportSubscriptions().contains(sportSubscription)){
                sportSubscription.getAthlete().removeSportSubscription(sportSubscription);
                athlete.addSportSubscription(sportSubscription);
                sportSubscription.setAthlete(athlete);
            }
            if(!rank.getSportSubscriptions().contains(sportSubscription)){
                sportSubscription.getRank().removeSportSubscription(sportSubscription);
                rank.addSportSubscription(sportSubscription);
                sportSubscription.setRank(rank);
            }
            sportSubscription.setName(name);
            em.merge(sportSubscription);
            return sportSubscription;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_SPORT_SUBSCRIPTION", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            SportSubscription sportSubscription = find(code);
            if(sportSubscription == null){
                throw new MyEntityNotFoundException("Sport Subscription with code '" + code + "' not found.");
            }
            Athlete athlete = sportSubscription.getAthlete();
            Rank rank = sportSubscription.getRank();
            if(athlete != null){
                athlete.removeSportSubscription(sportSubscription);
            }
            if(rank != null){
                rank.removeSportSubscription(sportSubscription);
            }
            em.remove(sportSubscription);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_SPORT_SUBSCRIPTION", e);
        }
    }

}
