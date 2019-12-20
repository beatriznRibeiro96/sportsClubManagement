package ejbs;

import entities.ActiveSport;
import entities.Rank;
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
    private ActiveSportBean activeSportBean;

    public Rank create (String name, int idadeMin, int idadeMax, int activeSportCode) throws MyEntityExistsException, MyEntityNotFoundException {
        ActiveSport activeSport = activeSportBean.find(activeSportCode);
        if(activeSport == null){
            throw new MyEntityNotFoundException("Active Sport not found");
        }
        Long count = (Long) em.createNamedQuery("countRanksByNameAndActiveSport").setParameter("name", name).setParameter("activeSport", activeSport).getSingleResult();
        if(count != 0){
            throw new MyEntityExistsException("Rank " + name + " already exists in " + activeSport.getName());
        }
        try {
            Rank rank = new Rank(name, idadeMin, idadeMax, activeSport);
            activeSport.addRank(rank);
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

    public Rank update(int code, String name, int idadeMin, int idadeMax, int activeSportCode) throws MyEntityNotFoundException, MyEntityExistsException {
        Rank rank = find(code);
        if(rank == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_RANK");
        }

        ActiveSport activeSport = activeSportBean.find(activeSportCode);
        if(activeSport == null){
            throw new MyEntityNotFoundException("Active Sport not found");
        }
        Long count = (Long) em.createNamedQuery("countRanksByNameAndActiveSport").setParameter("name", name).setParameter("activeSport", activeSport).getSingleResult();
        if(count != 0 && (!name.equals(rank.getName()) || activeSportCode != rank.getActiveSport().getCode())){
            throw new MyEntityExistsException(activeSport.getName() + " already has " + rank.getName());
        }
        try{
            em.lock(activeSport, LockModeType.OPTIMISTIC);
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
            rank.getActiveSport().removeRank(rank);
            em.remove(rank);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_RANK", e);
        }
    }
}
