package ejbs;

import entities.ActiveSport;
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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Stateless(name = "ScheduleEJB")
public class ScheduleBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private RankBean rankBean;

    @EJB
    private TrainingBean trainingBean;

    public Schedule create (String name, int dayOfWeek, String startTime, String endTime, int rankCode) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MyParseDateException, MyIllegalArgumentException {
        try {
            Rank rank = rankBean.find(rankCode);
            if(rank == null){
                throw new MyEntityNotFoundException("Rank not found.");
            }
            Long count = (Long) em.createNamedQuery("countSchedulesByNameAndRank").setParameter("name", name).setParameter("rank", rank).getSingleResult();
            if(count != 0){
                throw new MyEntityExistsException("'" + name + "' already exists in '" + rank.getName() + "'");
            }
            Set<Integer> daysValues = new LinkedHashSet<>(7);
            for (int i = 0; i <= 7; i++) {
                daysValues.add(i);
            }
            if(!daysValues.contains(dayOfWeek)){
                throw new MyIllegalArgumentException("Invalid day of week");
            }
            if (startTime.isEmpty() || endTime.isEmpty()){
                throw new MyParseDateException("Start time and end time cannot be empty");
            }
            DayOfWeek diaSemana = DayOfWeek.values()[dayOfWeek];
            LocalTime horaInicio = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime horaFim = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
            if(!horaInicio.isBefore(horaFim)){
                throw new MyIllegalArgumentException("Start time needs to be lower than end time");
            }
            Schedule schedule = new Schedule(name, diaSemana, horaInicio, horaFim, rank);
            em.persist(schedule);
            Training training = trainingBean.create("Treino " + name, rankCode, schedule.getCode());
            rank.addSchedule(schedule);
            return schedule;
        } catch (MyEntityExistsException | MyEntityNotFoundException | MyParseDateException | MyIllegalArgumentException e) {
            throw e;
        } catch(ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch(Exception e){
            throw new EJBException("ERROR_CREATING_SCHEDULE", e);
        }
    }

    public List<Schedule> all() {
        try {
            return (List<Schedule>) em.createNamedQuery("getAllSchedules").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SCHEDULES", e);
        }
    }

    public Schedule find(int code) {
        try {
            return em.find(Schedule.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_SCHEDULE",e);
        }
    }

    public Schedule update(int code, String name, int dayOfWeek, String startTime, String endTime, int rankCode) throws MyEntityNotFoundException, MyEntityExistsException, MyParseDateException, MyIllegalArgumentException {
        try{
            Schedule schedule = find(code);
            em.lock(schedule, LockModeType.OPTIMISTIC);
            if(schedule == null){
                throw new MyEntityNotFoundException("Schedule with code '" + code + "' not found.");
            }
            Rank rank = rankBean.find(rankCode);
            if(rank == null){
                throw new MyEntityNotFoundException("Active Sport not found.");
            }
            Long count = (Long) em.createNamedQuery("countSchedulesByNameAndRank").setParameter("name", name).setParameter("rank", rank).getSingleResult();
            if(count != 0 && (!name.equals(schedule.getName()) || rankCode != schedule.getRank().getCode())){
                throw new MyEntityExistsException("'" + name + "' already exists in '" + rank.getName() + "'");
            }
            Set<Integer> daysValues = new LinkedHashSet<>(7);
            for (int i = 0; i <= 7; i++) {
                daysValues.add(i);
            }
            if(!daysValues.contains(dayOfWeek)){
                throw new MyIllegalArgumentException("Invalid day of week");
            }
            if (startTime.isEmpty() || endTime.isEmpty()){
                throw new MyParseDateException("Start time and end time cannot be empty");
            }
            DayOfWeek diaSemana = DayOfWeek.values()[dayOfWeek];
            LocalTime horaInicio = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime horaFim = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
            if(!horaInicio.isBefore(horaFim)){
                throw new MyIllegalArgumentException("Start time needs to be lower than end time");
            }
            schedule.setName(name);
            schedule.setDayOfWeek(diaSemana);
            schedule.setStartTime(horaInicio);
            schedule.setEndTime(horaFim);
            if(!rank.getSchedules().contains(schedule)){
                schedule.getRank().removeSchedule(schedule);
                rank.addSchedule(schedule);
                schedule.setRank(rank);
            }
            em.merge(schedule);
            return schedule;
        } catch (MyEntityExistsException | MyEntityNotFoundException | MyParseDateException | MyIllegalArgumentException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_UPDATING_SCHEDULE", e);
        }
    }

    public void delete(int code) throws MyEntityNotFoundException {
        try{
            Schedule schedule = find(code);
            if(schedule == null){
                throw new MyEntityNotFoundException("Schedule with code '" + code + "' not found.");
            }
            schedule.getRank().removeSchedule(schedule);
            em.remove(schedule);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e){
            throw new EJBException("ERROR_DELETING_SCHEDULE", e);
        }
    }
}
