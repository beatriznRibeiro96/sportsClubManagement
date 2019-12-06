package ejbs;

import entities.Administrator;
import entities.Coach;
import entities.Sport;

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

    @PostConstruct
    public void PopulateDB(){
        try {
            Administrator administrator = administratorBean.create("admin1","admin","Joao","joao@mail.pt");
            Coach coach = coachBean.create("coach1","coach","Joana","joana@mail.pt");
            Sport sport = sportBean.create(1, "Futebol");
            sportBean.associate(1, "coach1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
