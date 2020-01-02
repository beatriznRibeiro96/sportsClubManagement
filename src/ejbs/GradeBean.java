package ejbs;

import entities.ActiveSport;
import entities.Athlete;
import entities.Grade;
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

@Stateless(name = "GradeBean")
public class GradeBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private ActiveSportBean activeSportBean;

    @EJB
    private AthleteBean athleteBean;

    public Grade create (String name, int activeSportCode) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        try {
            ActiveSport activeSport = activeSportBean.find(activeSportCode);
            if(activeSport == null){
                throw new MyEntityNotFoundException("Active Sport not found.");
            }
            Long count = (Long) em.createNamedQuery("countGradesByNameAndActiveSport").setParameter("name", name).setParameter("activeSport", activeSport).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException("'" + name + "' already exists in '" + activeSport.getName() + "'");
            }
            Grade grade = new Grade(name, activeSport);
            em.persist(grade);
            activeSport.addGrade(grade);
            return grade;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_GRADE", e);
        }
    }

    public List<Grade> all() {
        try {
            return (List<Grade>) em.createNamedQuery("getAllGrades").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_GRADES", e);
        }
    }

    public Grade find(int code) {
        try {
            return em.find(Grade.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_GRADE",e);
        }
    }

    public Grade update(int code, String name, int activeSportCode) throws MyEntityNotFoundException, MyEntityExistsException {
        try{
            Grade grade = find(code);
            em.lock(grade, LockModeType.OPTIMISTIC);
            if(grade == null){
                throw new MyEntityNotFoundException("Grade with code '" + code + "' not found.");
            }
            ActiveSport activeSport = activeSportBean.find(activeSportCode);
            if(activeSport == null){
                throw new MyEntityNotFoundException("Active Sport not found.");
            }
            Long count = (Long) em.createNamedQuery("countGradesByNameAndActiveSport").setParameter("name", name).setParameter("activeSport", activeSport).getSingleResult();
            if(count != 0 && (!name.equals(grade.getName()) || activeSportCode != grade.getActiveSport().getCode())){
                throw new MyEntityExistsException("'" + name + "' already exists in '" + activeSport.getName() + "'");
            }
            grade.setName(name);
            if(!activeSport.getGrades().contains(grade)){
                grade.getActiveSport().removeGrade(grade);
                activeSport.addGrade(grade);
                grade.setActiveSport(activeSport);
            }
            em.merge(grade);
            return grade;
        } catch (MyEntityExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_GRADE", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            Grade grade = find(code);
            if(grade == null){
                throw new MyEntityNotFoundException("Grade with code '" + code + "' not found.");
            }
            Set<Athlete> athletes = grade.getAthletes();
            for (Athlete athlete:athletes) {
                if(athlete.getGrades().contains(grade)){
                    athlete.removeGrade(grade);
                }
            }
            grade.getActiveSport().removeGrade(grade);
            em.remove(grade);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_GRADE", e);
        }
    }

    public void addAthlete(int gradeCode, String athleteUsername) throws MyEntityNotFoundException {
        try{
            Grade grade = find(gradeCode);
            if (grade == null) {
                throw new MyEntityNotFoundException("Grade with code: " + gradeCode + " not found.");
            }
            Athlete athlete = athleteBean.find(athleteUsername);
            if (athlete == null) {
                throw new MyEntityNotFoundException("Username '" + athleteUsername + "' not found.");
            }
            athlete.addGrade(grade);
            grade.addAthlete(athlete);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_ADD_ATHLETE", e);
        }
    }

    public void removeAthlete(int gradeCode, String athleteUsername) throws MyEntityNotFoundException {
        try{
            Grade grade = find(gradeCode);
            if (grade == null) {
                throw new MyEntityNotFoundException("Grade with code: " + gradeCode + " not found.");
            }
            Athlete athlete = athleteBean.find(athleteUsername);
            if (athlete == null) {
                throw new MyEntityNotFoundException("Username '" + athleteUsername + "' not found.");
            }
            athlete.removeGrade(grade);
            grade.removeAthlete(athlete);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_REMOVE_ATHLETE", e);
        }
    }
}
