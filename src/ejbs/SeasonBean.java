package ejbs;

import entities.Season;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "SeasonEJB")
public class SeasonBean {
    @PersistenceContext
    private EntityManager em;

    public Season create (String name) throws MyEntityExistsException, MyConstraintViolationException {
        try {
            Long count = (Long) em.createNamedQuery("countSeasonByName").setParameter("name", name).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException("Season name '" + name + "' already exists");
            }
            Season season = new Season(name);
            em.persist(season);
            return season;
        } catch (MyEntityExistsException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
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
        try{
            Season season = find(code);
            em.lock(season, LockModeType.OPTIMISTIC);
            if(season == null){
                throw new MyEntityNotFoundException("Season '" + name + "' not found.");
            }
            Long count = (Long) em.createNamedQuery("countSeasonByName").setParameter("name", name).getSingleResult();
            if(count != 0 && !name.equals(season.getName())){
                throw new MyEntityExistsException("Season '" + name + "' already exists");
            }
            season.setName(name);
            em.merge(season);
            return season;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_SEASON", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            Season season = find(code);
            if(season == null){
                throw new MyEntityNotFoundException("Season with code '" + code + "' not found.");
            }
            em.remove(season);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_SEASON", e);
        }
    }
}
