package ejbs;

import entities.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton(name = "ConfigEJB")
public class ConfigBean {
    @EJB
    private AdministratorBean administratorBean;

    @EJB
    private CoachBean coachBean;

    @EJB
    private SportBean sportBean;

    @EJB
    private PartnerBean partnerBean;

    @EJB
    private AthleteBean athleteBean;

    @EJB
    private RankBean rankBean;

    @PostConstruct
    public void PopulateDB(){
        try {
            Administrator administrator = administratorBean.create("admin1","admin","Joao","joao@mail.pt");
            Coach coach = coachBean.create("coach1","coach","Joana","joana@mail.pt");
            Sport sport = sportBean.create(1, "Futebol");
            Partner partner = partnerBean.create("partner1", "partner", "Miguel", "miguel@mail.pt");
            Athlete athlete = athleteBean.create("athlete1", "athlete", "Rui", "rui@mail.pt");
            Rank rank = rankBean.create(1, "Futebol-Seniores", sport.getCode());
            Rank rank1 = rankBean.create(2, "Futebol-Junior", sport.getCode());
            rankBean.update(rank1.getCode(), "Futebol-Juniores", sport.getCode());
            sportBean.associateCoach(sport.getCode(), coach.getUsername());
            sportBean.associateAthlete(sport.getCode(), athlete.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
