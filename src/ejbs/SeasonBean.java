package ejbs;

import entities.Season;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "SeasonEJB")
public class SeasonBean {
    @PersistenceContext
    private EntityManager em;

    public Season create (String name) throws MyEntityExistsException {
        Long count = (Long) em.createNamedQuery("countSeasonByName").setParameter("name", name).getSingleResult();
        if(count != 0){
            throw new MyEntityExistsException("Season " + name + " already exists");
        }
        try {
            Season season = new Season(name);
            em.persist(season);
            return season;
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_SEASON", e);
        }
    }

    public List<Season> all() {
        try {
            return (List<Season>) em.createNamedQuery("getAllSeasons").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SEASONS", e);
        }
    }

    public Season find(int code) {
        try {
            return em.find(Season.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_SEASON",e);
        }
    }

    public Season update(int code, String name) throws MyEntityNotFoundException, MyEntityExistsException {
        Season season = find(code);
        if(season == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_SEASON");
        }
        Long count = (Long) em.createNamedQuery("countSeasonByName").setParameter("name", name).getSingleResult();
        if(count != 0 && !name.equals(season.getName())){
            throw new MyEntityExistsException("Season " + name + " already exists");
        }
        try{
            em.lock(season, LockModeType.OPTIMISTIC);
            season.setName(name);
            em.merge(season);
            return season;
        }catch (Exception e){
            throw new EJBException("ERROR_UPDATING_SEASON", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        Season season = find(code);
        if(season == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_SEASON");
        }
        try{
            em.lock(season, LockModeType.OPTIMISTIC);
            em.remove(season);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_SEASON", e);
        }
    }
}
