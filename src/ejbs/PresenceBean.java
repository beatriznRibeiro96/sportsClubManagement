package ejbs;

import entities.Athlete;
import entities.Presence;
import entities.Coach;
import entities.Training;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

@Stateless(name = "PresenceEJB")
public class PresenceBean {

    @PersistenceContext
    private EntityManager em;

    public Presence create(String code, String codeTreino, int dia, int mes, int ano, List<String> emailsAthletes, String emailTreinador){
        try{
            Presence presence = em.find(Presence.class,code);
            if(presence != null){
                throw new MyEntityAlreadyExistsException("Presenca com o codigo: " + code + " já existe");
            }
            Coach coach = em.find(Coach.class,emailTreinador);
            if(coach == null){
                throw new MyEntityNotFoundException("Coach email: " + emailTreinador + " não existe.");
            }
            Coach coach = em.find(Coach.class,codeTreino);
            if(coach == null){
                throw new MyEntityNotFoundException("Training code: " + codeTreino + " não existe.");
            }
            List <Athlete> atheletes = geraAthletesPresentes(emailsAthletes);
            presence = new Presence(code,treino,new GregorianCalendar(dia,mes,ano),coach);
            presence.setAthletesPresentes(atheletes);
            em.persist(presence);
            return presence;
        }catch(Exception e){
            throw new NullPointerException(e.getMessage());
        }
    }

    public Presence update (String code, String codeTreino, int dia, int mes, int ano,List<String> emailsAthletes, String emailTreinador) throws MyEntityNotFoundException {
        try{
            Presence presence = em.find(Presence.class,code);
            if(presence == null){
                throw new MyEntityNotFoundException("Presença não encontrada!");
            }
            em.lock(presence, LockModeType.OPTIMISTIC);

            Coach coach = em.find(Coach.class,emailTreinador);
            if(coach == null){
                throw new MyEntityNotFoundException("Coach email: " + emailTreinador + " not exist.");
            }
            Coach coach = em.find(Coach.class,codeTreino);
            if(coach == null){
                throw new MyEntityNotFoundException("Training code: " + codeTreino + " not exist.");
            }
            List <Athlete0> athletes = geraAthletesPresentes(emailsAthletes);

            presence.setCode(code);
            presence.setTreinador(treinador);
            presence.setTraining(training);
            presence.setDataTraining(new GregorianCalendar(dia,mes,ano));
            presence.setAthletesPresentes(athletes);

            em.merge(presence);

            return presence;
        }catch(MyEntityNotFoundException e){
            throw e;
        }catch(Exception e){
            throw new EJBException("ERROR UPDATING PRESENCE", e);
        }
    }

    public List <Presence> all(){
        try{
            return (List<Presence>) em.createNamedQuery("getAllPresences").getResultList();
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PRESENCES", e);
        }
    }

    public Presence findPresenca (String code){
        try{
            return em.find(Presence.class,code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PRESENCE", e);
        }
    }

    public void delete(String code) throws MyEntityNotFoundException{
        try {
            Presence presence = em.find(Presence.class,code);
            if (presence == null) {
                throw new MyEntityNotFoundException("Presenca com o codigo: " + code + " não existe");
            }
            em.remove(findPresenca(code));
        }catch (MyEntityNotFoundException e) {
            throw e;
        }catch (Exception e){
            throw new EJBException("ERROR_DELETING_PRESENCA",e);
        }
    }

    public List <Presence> findPresencasTreino(String codeTreino) throws MyEntityNotFoundException {
        try{
            Training training = em.find(Training.class,codeTreino);
            if(training==null){
                throw new MyEntityNotFoundException("Training code:  " + codeTreino + " not exist");

            }
            List <Presence> presences = this.all();
            List <Presence> presencesTraining = new LinkedList<>();
            for(Presence presence:presences){
                if(presence.getTraining().equals(training)){
                    presencesTraining.add(presence);
                }
            }
            return presencesTraining;
        }catch (MyEntityNotFoundException e) {
            throw e;
        }catch (Exception e){
            throw new EJBException("ERROR_FINDING_PRESENCES_FROM_Training",e);
        }
    }

    private List<Athlete> geraAthletesPresentes(List<String> emailsAthletes) {
        List<Athlete> athletes = new LinkedList<>();
        Athlete athlete = null;
        for (String email:emailsAthletes) {
            athlete = em.find(Athlete.class,email);
            if(athlete != null){
                athletes.add(athlete);
            }
        }
        return athletes;
    }
}