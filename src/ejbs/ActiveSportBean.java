package ejbs;

import entities.*;
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

@Stateless(name = "ActiveSportEJB")
public class ActiveSportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;
    @EJB
    private SeasonBean seasonBean;

    public ActiveSport create (String name, int sportCode, int seasonCode) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Sport sport = sportBean.find(sportCode);
            if(sport == null){
                throw new MyEntityNotFoundException("Sport not found.");
            }
            Season season = seasonBean.find(seasonCode);
            if(season == null){
                throw new MyEntityNotFoundException("Season not found.");
            }
            Long count = (Long) em.createNamedQuery("countActiveSportBySportAndSeason").setParameter("sport", sport).setParameter("season", season).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException("'" + sport.getName() + "' is already active for season " + season.getName());
            }
            ActiveSport activeSport = new ActiveSport(name, sport, season);
            em.persist(activeSport);
            return activeSport;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
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

    public ActiveSport update(int code, String name, int sportCode, int seasonCode) throws MyEntityNotFoundException, MyEntityExistsException {
        try{
            ActiveSport activeSport = find(code);
            em.lock(activeSport, LockModeType.OPTIMISTIC);
            if(activeSport == null){
                throw new MyEntityNotFoundException("Active Sport with code '" + code + "' not found.");
            }
            Sport sport = sportBean.find(sportCode);
            if(sport == null){
                throw new MyEntityNotFoundException("Sport not found.");
            }
            Season season = seasonBean.find(seasonCode);
            if(season == null){
                throw new MyEntityNotFoundException("Season not found.");
            }
            Long count = (Long) em.createNamedQuery("countActiveSportBySportAndSeason").setParameter("sport", sport).setParameter("season", season).getSingleResult();
            if(count != 0 && (sportCode != activeSport.getSport().getCode() || seasonCode != activeSport.getSeason().getCode())){
                throw new MyEntityExistsException("'" + sport.getName() + "' is already active for season " + season.getName());
            }
            activeSport.setName(name);
            activeSport.setSport(sport);
            activeSport.setSeason(season);
            em.merge(activeSport);
            return activeSport;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_ACTIVE_SPORT", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            ActiveSport activeSport = find(code);
            if(activeSport == null){
                throw new MyEntityNotFoundException("Active Sport with code '" + code + "' not found.");
            }
            activeSport.getRanks().clear();
            em.remove(activeSport);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_ACTIVE_SPORT", e);
        }
    }
}
