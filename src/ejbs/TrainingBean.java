package ejbs;

import entities.Athlete;
import entities.Rank;
import entities.Schedule;
import entities.Training;
import exceptions.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "TrainingEJB")
public class TrainingBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private RankBean rankBean;

    @EJB
    private ScheduleBean scheduleBean;

    @EJB
    private AthleteBean athleteBean;

    public Training create (String name, int rankCode, int scheduleCode) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        try {
            Rank rank = rankBean.find(rankCode);
            if(rank == null){
                throw new MyEntityNotFoundException("Rank not found.");
            }
            Schedule schedule = scheduleBean.find(scheduleCode);
            if(schedule == null){
                throw new MyEntityNotFoundException("Schedule not found.");
            }
            Long count = (Long) em.createNamedQuery("countTrainingByName").setParameter("name", name).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException("'" + name + "' already exists");
            }
            Training training = new Training(name, rank, schedule);
            rank.addTraining(training);
            em.persist(training);
            return training;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_TRAINING", e);
        }
    }

    public List<Training> all() {
        try {
            return (List<Training>) em.createNamedQuery("getAllTrainings").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_TRAININGS", e);
        }
    }

    public Training find(int code) {
        try {
            return em.find(Training.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_TRAINING",e);
        }
    }

    public Training update(int code, String name, int rankCode, int scheduleCode) throws MyEntityNotFoundException, MyEntityExistsException {
        try{
            Training training = find(code);
            em.lock(training, LockModeType.OPTIMISTIC);
            if(training == null){
                throw new MyEntityNotFoundException("Training '" + name + "' not found.");
            }
            Rank rank = rankBean.find(rankCode);
            if(rank == null){
                throw new MyEntityNotFoundException("Rank not found.");
            }
            Schedule schedule = scheduleBean.find(scheduleCode);
            if(schedule == null){
                throw new MyEntityNotFoundException("Schedule not found.");
            }
            Long count = (Long) em.createNamedQuery("countTrainingByName").setParameter("name", name).getSingleResult();
            if(count != 0 && !name.equals(training.getName())){
                throw new MyEntityExistsException("'" + name + "' already exists");
            }
            training.setName(name);
            training.setSchedule(schedule);
            if(!rank.getTrainings().contains(training)){
                training.getRank().removeTraining(training);
                rank.addTraining(training);
            }
            training.setRank(rank);
            em.merge(training);
            return training;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_TRAINING", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            Training training = find(code);
            if(training == null){
                throw new MyEntityNotFoundException("Training with code '" + code + "' not found.");
            }
            training.getRank().removeTraining(training);
            em.remove(training);
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_TRAINING", e);
        }
    }

    public void addPresenceToTraining(String athleteUsername, int trainingCode) throws MyEntityNotFoundException, MyIllegalArgumentException {
        try{
            Training training = find(trainingCode);
            if (training == null) {
                throw new MyEntityNotFoundException("Code '" + trainingCode + "' not found.");
            }
            Athlete athlete = athleteBean.find(athleteUsername);
            if (athlete == null) {
                throw new MyEntityNotFoundException("Athlete not found.");
            }
            if(training.getPresences().contains(athlete)){
                throw new MyIllegalArgumentException("Athlete is already present in training with code '" + trainingCode + "'");
            }
            training.addPresence(athlete);
        } catch (MyEntityNotFoundException | MyIllegalArgumentException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_ASSOCIATE_PRESENCE_TO_TRAINING", e);
        }
    }

    public void removePresenceFromTraining(String athleteUsername, int trainingCode) throws MyEntityNotFoundException {
        try{
            Training training = find(trainingCode);
            if (training == null) {
                throw new MyEntityNotFoundException("Code '" + trainingCode + "' not found.");
            }
            Athlete athlete = athleteBean.find(athleteUsername);
            if (athlete == null) {
                throw new MyEntityNotFoundException("Athlete not found.");
            }
            training.removePresence(athlete);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DISSOCIATE_PRESENCE_FROM_TRAINING", e);
        }
    }
}
