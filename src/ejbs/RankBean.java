package ejbs;

import entities.Rank;
import entities.Sport;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "RankEJB")
public class RankBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;

    public Rank create (int code, String name, int sportCode) throws MyEntityExistsException, MyEntityNotFoundException {
        if (find(code)!=null){
            throw new MyEntityExistsException("Code '" + code + "' already exists");
        }
        Sport sport = sportBean.find(sportCode);
        if(sport == null){
            throw new MyEntityNotFoundException("Sport with code '" + sportCode + "' doesn't exist");
        }
        try {
            Rank rank = new Rank(code, name, sport);
            sport.addRank(rank);
            em.persist(rank);
            return rank;
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

    public Rank update(int code, String name, int sportCode) throws MyEntityNotFoundException {
        Rank rank = find(code);
        Sport sport = sportBean.find(sportCode);
        if(rank == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_RANK");
        }
        try{
            em.lock(rank, LockModeType.OPTIMISTIC);
            rank.setName(name);
            rank.setSport(sport);
            if(!sport.getRanks().contains(rank)){
                sport.addRank(rank);
            }
            em.merge(rank);
            return rank;
        }catch (Exception e){
            throw new EJBException("ERROR_UPDATING_RANK", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        Rank rank = find(code);
        if(rank == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_RANK");
        }
        try{
            em.lock(rank, LockModeType.OPTIMISTIC);
            em.remove(rank);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_RANK", e);
        }
    }
}
