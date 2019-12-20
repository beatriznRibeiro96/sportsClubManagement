package ejbs;

import entities.ActiveSport;
import entities.Coach;
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
import java.util.Set;

@Stateless(name = "ActiveSportEJB")
public class ActiveSportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;
    @EJB
    private SeasonBean seasonBean;
    @EJB
    private CoachBean coachBean;

    public ActiveSport create (String name, int sportCode, int seasonCode) throws MyEntityExistsException, MyEntityNotFoundException {
        Sport sport = sportBean.find(sportCode);
        if(sport == null){
            throw new MyEntityNotFoundException("Sport not found");
        }
        Season season = seasonBean.find(seasonCode);
        if(season == null){
            throw new MyEntityNotFoundException("Season not found");
        }
        Long count = (Long) em.createNamedQuery("countActiveSportBySportAndSeason").setParameter("sport", sport).setParameter("season", season).getSingleResult();
        if(count != 0){
            throw new MyEntityExistsException("Sport " + sport.getName() + " is already active for season " + season.getName());
        }
        try {
            ActiveSport activeSport = new ActiveSport(name, sport, season);
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

    public ActiveSport update(int code, String name, int sportCode, int seasonCode) throws MyEntityNotFoundException, MyEntityExistsException {
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
        Long count = (Long) em.createNamedQuery("countActiveSportBySportAndSeason").setParameter("sport", sport).setParameter("season", season).getSingleResult();
        if(count != 0 && (sportCode != activeSport.getSport().getCode() || seasonCode != activeSport.getSeason().getCode())){
            throw new MyEntityExistsException(sport.getName() + " is already active for season " + season.getName());
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
        Set<Coach> coaches = activeSport.getCoaches();
        try{
            em.lock(activeSport, LockModeType.OPTIMISTIC);
            if(coaches != null){
                for (Coach coach:coaches) {
                    coach.removeActiveSport(activeSport);
                }
            }
            em.remove(activeSport);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_ACTIVE_SPORT", e);
        }
    }

    public void associateCoach(int activeSportCode, String coachUsername){
        try{
            ActiveSport activeSport = find(activeSportCode);
            Coach coach = coachBean.find(coachUsername);
            activeSport.addCoach(coach);
            coach.addActiveSport(activeSport);
        } catch (Exception e){
            throw new EJBException("ERROR_ASSOCIATE_COACH", e);
        }
    }

    public void dissociateCoach(int activeSportCode, String coachUsername){
        try{
            ActiveSport activeSport = find(activeSportCode);
            Coach coach = coachBean.find(coachUsername);
            activeSport.removeCoach(coach);
            coach.removeActiveSport(activeSport);
        } catch (Exception e){
            throw new EJBException("ERROR_DISSOCIATE_COACH", e);
        }
    }
}
