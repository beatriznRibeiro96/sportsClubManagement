package ejbs;

import entities.ActiveSport;
import entities.Coach;
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
import java.util.Set;

@Stateless(name = "RankEJB")
public class RankBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private ActiveSportBean activeSportBean;

    @EJB
    private CoachBean coachBean;

    public Rank create (String name, int idadeMin, int idadeMax, int activeSportCode) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        try {
            ActiveSport activeSport = activeSportBean.find(activeSportCode);
            if(activeSport == null){
                throw new MyEntityNotFoundException("Active Sport not found.");
            }
            Long count = (Long) em.createNamedQuery("countRanksByNameAndActiveSport").setParameter("name", name).setParameter("activeSport", activeSport).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException("'" + name + "' already exists in '" + activeSport.getName() + "'");
            }
            Rank rank = new Rank(name, idadeMin, idadeMax, activeSport);
            em.persist(rank);
            activeSport.addRank(rank);
            return rank;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_RANK", e);
        }
    }

    public List<Rank> all() {
        try {
            return (List<Rank>) em.createNamedQuery("getAllRanks").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_RANKS", e);
        }
    }

    public Rank find(int code) {
        try {
            return em.find(Rank.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_RANK",e);
        }
    }

    public Rank update(int code, String name, int idadeMin, int idadeMax, int activeSportCode) throws MyEntityNotFoundException, MyEntityExistsException {
        try{
            Rank rank = find(code);
            em.lock(rank, LockModeType.OPTIMISTIC);
            if(rank == null){
                throw new MyEntityNotFoundException("Rank with code '" + code + "' not found.");
            }
            ActiveSport activeSport = activeSportBean.find(activeSportCode);
            if(activeSport == null){
                throw new MyEntityNotFoundException("Active Sport not found.");
            }
            Long count = (Long) em.createNamedQuery("countRanksByNameAndActiveSport").setParameter("name", name).setParameter("activeSport", activeSport).getSingleResult();
            if(count != 0 && (!name.equals(rank.getName()) || activeSportCode != rank.getActiveSport().getCode())){
                throw new MyEntityExistsException("'" + name + "' already exists in '" + activeSport.getName() + "'");
            }
            rank.setName(name);
            rank.setIdadeMin(idadeMin);
            rank.setIdadeMax(idadeMax);
            if(!activeSport.getRanks().contains(rank)){
                rank.getActiveSport().removeRank(rank);
                activeSport.addRank(rank);
                rank.setActiveSport(activeSport);
            }
            em.merge(rank);
            return rank;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_RANK", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            Rank rank = find(code);
            if(rank == null){
                throw new MyEntityNotFoundException("Rank with code '" + code + "' not found.");
            }
            Set<Coach> coaches = rank.getCoaches();
            if(coaches != null){
                for (Coach coach:coaches) {
                    coach.removeRank(rank);
                }
            }
            Set<SportSubscription> sportSubscriptions = rank.getSportSubscriptions();
            if(sportSubscriptions != null){
                for (SportSubscription sportSubscription:sportSubscriptions) {
                    sportSubscription.getAthlete().removeSportSubscription(sportSubscription);
                }
            }
            rank.getActiveSport().removeRank(rank);
            em.remove(rank);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_RANK", e);
        }
    }

    public void associateCoach(int rankCode, String coachUsername) throws MyEntityNotFoundException {
        try{
            Rank rank = find(rankCode);
            if (rank == null) {
                throw new MyEntityNotFoundException("Rank with code: " + rankCode + " not found.");
            }
            Coach coach = coachBean.find(coachUsername);
            if (coach == null) {
                throw new MyEntityNotFoundException("Username '" + coachUsername + "' not found.");
            }
            coach.addRank(rank);
            rank.addCoach(coach);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_ASSOCIATE_COACH", e);
        }
    }

    public void dissociateCoach(int rankCode, String coachUsername) throws MyEntityNotFoundException {
        try{
            Rank rank = find(rankCode);
            if (rank == null) {
                throw new MyEntityNotFoundException("Rank with code: " + rankCode + " not found.");
            }
            Coach coach = coachBean.find(coachUsername);
            if (coach == null) {
                throw new MyEntityNotFoundException("Username '" + coachUsername + "' not found.");
            }
            coach.removeRank(rank);
            rank.removeCoach(coach);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DISSOCIATE_COACH", e);
        }
    }
}
