package ejbs;

import dtos.ProductDTO;
import entities.Category;
import entities.Product;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


@Stateless(name = "TrainingEJB")

public class TrainingBean {

    @PersistenceContext
    EntityManager em;


    public Training create(String code, String emailCoach, String codeGrade, Schedule horaInicio, Schedule horaFim, Schedule diaSemana) {
        try {
            if(em.find(Training.class, code) != null){
                throw new MyEntityAlreadyExistsException("Training code: " + code + " already exists");
            }
            Coach coach = em.find(Coach.class,emailCoach);
            if(coach == null){
                throw new MyEntityNotFoundException("Coach email: " + emailCoach + " not exist.");
            }

            Grade grade = em.find(Grade.class,codeGraduacao);
            if(grade == null){
                throw new MyEntityNotFoundException("Grade id: " + codeGrade + "  not exist.");
            }

            Training training = new Training(code,treinador,graduacao,horaInicio,horaFim,diaSemana);
            em.persist(training);
            return  training;
        }catch(Exception e){
            throw new NullPointerException(e.getMessage());
        }
    }

    public List<Training> all(){
        try{
            return (List<Training>) em.createNamedQuery("getAllTrainings").getResultList();
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_TRAININGS", e);
        }
    }


    public Training find(String id) {
        try{
            return em.find(Training.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_TRAINING", e);
        }
    }

    public Training update(String code, String emaiCoach, String codeGrade, Schedule horaInicio, Schedule horaFim, Schedule diaSemana)  {
        try {
            Training training = em.find(Training.class,code);
            if(training == null){
                throw new MyEntityNotFoundException("Training not found!");
            }

            em.lock(training, LockModeType.OPTIMISTIC);

            Coach coach = em.find(Coach.class,emaiCoach);
            if(coach == null){
                throw new MyEntityNotFoundException("Coach email: " + emailCoach + " not exist.");
            }

            Grade grade = em.find(Grade.class,codeGrade);
            if(grade == null){
                throw new MyEntityNotFoundException("Grade id: " + codeGrade + "  not exist.");
            }

            training.setCode(code);
            training.setCoach(coach);

            training.setGrade(grade);

            training.setHoraInicio(horaInicio);
            training.setHoraFim(horaFim);
            training.setDiaSemana(diaSemana);

            em.merge(training);

            return training;

        }catch(MyEntityNotFoundException e){
            throw e;
        }catch(Exception e){
            throw new EJBException("ERROR UPDATING Training", e);
        }
    }

    public void delete(String code){
        try {
            em.remove(findTraining(code));
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_TRAINING",e);
        }
    }
    public Set <Presence> getListPresences(String code) throws MyEntityNotFoundException {
        try{
            Training training = (Training) em.find(Training.class, code);
            if (training == null) {
                throw new MyEntityNotFoundException("Training code " + code + " not exist.");
            }
            return training.getPresences();
        }catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR : + e.getMessage());
        }
    }
}
