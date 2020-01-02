package ejbs;

import entities.ActiveSport;
import entities.Coach;
import entities.Rank;
import entities.Sport;
import exceptions.*;


import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Stateless(name = "CoachEJB")
public class CoachBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private RankBean rankBean;

    public Coach create (String username,String password, String name, String email, String birthDate) throws MyEntityExistsException, MyConstraintViolationException, MyParseDateException {
        try {
            if (find(username)!=null){
                throw new MyEntityExistsException("Username '" + username + "' already exists");
            }
            if (birthDate.isEmpty()){
                throw new MyParseDateException("Birth date cannot be empty");
            }
            LocalDate dataNascimento = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Coach coach = new Coach(username, password, name, email, dataNascimento);
            em.persist(coach);
            return coach;
        } catch(MyEntityExistsException | MyParseDateException e){
            throw e;
        } catch(ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_COACH", e);
        }
    }

    public List<Coach> all() {
        try {
            return (List<Coach>) em.createNamedQuery("getAllCoaches").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_COACHES", e);
        }
    }

    public Coach find(String username) {
        try {
            return em.find(Coach.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_COACH",e);
        }
    }

    public Coach update(String username, String password, String name, String email, String birthDate) throws MyEntityNotFoundException, MyParseDateException {
        try{
            Coach coach = find(username);
            em.lock(coach, LockModeType.OPTIMISTIC);
            if(coach == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            if (birthDate.isEmpty()){
                throw new MyParseDateException("Birth date cannot be empty");
            }
            LocalDate dataNascimento = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            coach.setName(name);
            coach.setEmail(email);
            coach.setPassword(password);
            coach.setBirthDate(dataNascimento);
            em.merge(coach);
            return coach;
        } catch (MyEntityNotFoundException | MyParseDateException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_COACH", e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        try{
            Coach coach = find(username);
            if(coach == null){
                throw new MyEntityNotFoundException("Username '" + username + "' not found.");
            }
            Set<Rank> ranks = coach.getRanks();
            if(ranks != null){
                for (Rank rank:ranks) {
                    rank.removeCoach(coach);
                }
            }
            em.remove(coach);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_COACH", e);
        }
    }

    public void associateCoachToRank(String coachUsername, int rankCode) throws MyEntityNotFoundException, MyIllegalArgumentException {
        try{
            Coach coach = find(coachUsername);
            if (coach == null) {
                throw new MyEntityNotFoundException("Username '" + coachUsername + "' not found.");
            }
            Rank rank = rankBean.find(rankCode);
            if (rank == null) {
                throw new MyEntityNotFoundException("Rank not found.");
            }
            if(coach.getRanks().contains(rank)){
                throw new MyIllegalArgumentException("Coach is already enrolled in rank with code '" + rankCode + "'");
            }
            coach.addRank(rank);
            rank.addCoach(coach);
        } catch (MyEntityNotFoundException | MyIllegalArgumentException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_ASSOCIATE_COACH_TO_RANK", e);
        }
    }

    public void dissociateCoachFromRank(String coachUsername, int rankCode) throws MyEntityNotFoundException {
        try{
            Coach coach = find(coachUsername);
            if (coach == null) {
                throw new MyEntityNotFoundException("Username '" + coachUsername + "' not found.");
            }
            Rank rank = rankBean.find(rankCode);
            if (rank == null) {
                throw new MyEntityNotFoundException("Rank not found.");
            }
            rank.removeCoach(coach);
            coach.removeRank(rank);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DISSOCIATE_COACH_FROM_RANK", e);
        }
    }
}
