package ejbs;

import entities.ActiveSport;
import entities.Season;
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

@Stateless(name = "ActiveSportEJB")
public class ActiveSportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;
    @EJB
    private SeasonBean seasonBean;

    public ActiveSport create (int code, String name, int sportCode, int seasonCode) throws MyEntityExistsException, MyEntityNotFoundException {
        if (find(code)!=null){
            throw new MyEntityExistsException("Code '" + code + "' already exists");
        }
        Sport sport = sportBean.find(sportCode);
        if(sport == null){
            throw new MyEntityNotFoundException("Sport not found");
        }
        Season season = seasonBean.find(seasonCode);
        if(season == null){
            throw new MyEntityNotFoundException("Season not found");
        }
        try {
            ActiveSport activeSport = new ActiveSport(code, name, sport, season);
            em.persist(activeSport);
            return activeSport;
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_ACTIVE_SPORT", e);
        }
    }

    public List<ActiveSport> all() {
        try {
            return (List<ActiveSport>) em.createNamedQuery("getAllActiveSports").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ACTIVE_SPORTS", e);
        }
    }

    public ActiveSport find(int code) {
        try {
            return em.find(ActiveSport.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_ACTIVE_SPORT",e);
        }
    }

    public ActiveSport update(int code, String name, int sportCode, int seasonCode) throws MyEntityNotFoundException {
        ActiveSport activeSport = find(code);
        if(activeSport == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_ACTIVE_SPORT");
        }

        Sport sport = sportBean.find(sportCode);
        if(sport == null){
            throw new MyEntityNotFoundException("Sport not found");
        }
        Season season = seasonBean.find(seasonCode);
        if(season == null){
            throw new MyEntityNotFoundException("Season not found");
        }
        try{
            em.lock(activeSport, LockModeType.OPTIMISTIC);
            activeSport.setName(name);
            activeSport.setSport(sport);
            activeSport.setSeason(season);
            em.merge(activeSport);
            return activeSport;
        }catch (Exception e){
            throw new EJBException("ERROR_UPDATING_ACTIVE_SPORT", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        ActiveSport activeSport = find(code);
        if(activeSport == null){
            throw new MyEntityNotFoundException("ERROR_FINDING_ACTIVE_SPORT");
        }
        try{
            em.lock(activeSport, LockModeType.OPTIMISTIC);
            em.remove(activeSport);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_ACTIVE_SPORT", e);
        }
    }
}
