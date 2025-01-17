package ejbs;

import entities.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton(name = "ConfigEJB")
public class ConfigBean {
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @EJB
    private AdministratorBean administratorBean;

    @EJB
    private CoachBean coachBean;

    @EJB
    private SportBean sportBean;

    @EJB
    private ProductBean productBean;

    @EJB
    private CategoryBean categoryBean;

    @EJB
    private UserBean userBean;

    @EJB
    private OrderBean orderBean;

    @EJB
    private LineItemOrderBean lineItemOrderBean;

    @EJB
    private PaymentBean paymentBean;

    @EJB
    private MethodPaymentBean methodPaymentBean;

    @EJB
    private PartnerBean partnerBean;

    @EJB
    private AthleteBean athleteBean;

    @EJB
    private SeasonBean seasonBean;

    @EJB
    private ActiveSportBean activeSportBean;

    @EJB
    private SportSubscriptionBean sportSubscriptionBean;

    @EJB
    private RankBean rankBean;

    @EJB
    private GradeBean gradeBean;

    @EJB
    private ScheduleBean scheduleBean;

    @EJB
    private TrainingBean trainingBean;

    @PostConstruct
    public void PopulateDB(){
        try {
            Administrator administrator = administratorBean.create("admin1","admin","Joao","joao@mail.pt", "1989-03-12");

            Category category1 = categoryBean.create("Artigo desportivo");
            Category category2 = categoryBean.create("Seguro");
            Category category3 = categoryBean.create("Graduação");//Por exemplo: uniformes
            Category category4 = categoryBean.create("Inscrição (Joia)");//Pagamento Anual
            Category category5 = categoryBean.create("Quota");//Pagamento Mensal
            Category category6 = categoryBean.create("Aula");
            Category category7 = categoryBean.create("Estágio");
            Category category8 = categoryBean.create("Outro");

            Product product1_to_invalid = productBean.create("Bola",19.90, category1);
            Product product2 = productBean.create("Bola Basquetebol",19.90, category1);
            Product product3 = productBean.create("Seguro 6 em 6 meses", 3.5, category2);
            Product product4 = productBean.create("Seguro 3 em 3 meses",5.0, category2);

            //Product product1 = productBean.update(product1_to_invalid.getId(), "Bola Futebol", 19.90, category1);
            //if (product1 != null) categoryBean.setProductInCategory(product1.getId(), category1.getId());

            Order order1 = orderBean.create(administrator);
            LineItemOrder lineItemOrder1 = lineItemOrderBean.create(product1_to_invalid, order1, 2);
            LineItemOrder lineItemOrder2 = lineItemOrderBean.create(product2, order1, 1);
            orderBean.setLineItemInOrder(lineItemOrder1.getId(), order1.getId());
            orderBean.setLineItemInOrder(lineItemOrder2.getId(), order1.getId());
            orderBean.updateTotalPrice(order1.getId());
            userBean.setOrderInUser(order1.getId(), administrator.getUsername());

            MethodPayment methodPayment1 = methodPaymentBean.create("PAYPAL");
            Payment payment1 = paymentBean.create(23.5, order1, methodPayment1);
            orderBean.setPaymentInOrder(payment1.getId(), order1.getId());
            orderBean.updatePayed(order1.getId());

            //region Atletas, sócios, treinadores e modalidades
            Coach coach = coachBean.create("coach1","coach","Joana","joana@mail.pt", "1990-05-20");
            Coach coach2 = coachBean.create("coach2", "coach", "Maria", "maria@mail.pt", "1997-04-07");
            Partner partner = partnerBean.create("partner1", "partner", "Miguel", "miguel@mail.pt", "1987-09-23");
            Athlete athlete = athleteBean.create("athlete1", "athlete", "Rui", "rui@mail.pt", "1995-11-30");
            Athlete athlete2 = athleteBean.create("athlete2", "athlete", "Luís", "luis@mail.pt", "1997-02-17");
            Season season = seasonBean.create("18/19");
            Sport sport = sportBean.create("Futebol");
            Sport sport2 = sportBean.create("Andebol");
            ActiveSport activeSport = activeSportBean.create("Futebol-18/19", sport.getCode(), season.getCode());
            ActiveSport activeSport2 = activeSportBean.create("Andebol-18/19", sport2.getCode(), season.getCode());
            Rank rank1 = rankBean.create("Juniores-Futebol-18/19", 16, 19, activeSport.getCode());
            Rank rank2 = rankBean.create("Seniores-Futebol-18/19", 16, 35, activeSport.getCode());
            Rank rank3 = rankBean.create("Seniores-Andebol-18/19", 18, 40, activeSport2.getCode());
            SportSubscription sportSubscription = sportSubscriptionBean.create("Seniores-And18/19-Rui", rank3.getCode(), athlete.getUsername());
            SportSubscription sportSubscription2 = sportSubscriptionBean.create("Seniores-Fut18/19-Rui", rank2.getCode(), athlete.getUsername());
            SportSubscription sportSubscription3 = sportSubscriptionBean.create("Juniores-Fut18/19-Luís", rank1.getCode(), athlete2.getUsername());
            Grade grade = gradeBean.create("Formação Completa", activeSport.getCode());
            Grade grade2 = gradeBean.create("Atleta com distinção", activeSport.getCode());
            Grade grade3 = gradeBean.create("Goleador 18/19", activeSport2.getCode());
            Schedule schedule = scheduleBean.create("Futebol Seniores Manhã Segunda", 1, "09:30", "12:30", rank2.getCode());
            Schedule schedule2 = scheduleBean.create("Futebol Juniores Tarde Segunda", 1, "14:30", "17:30", rank1.getCode());
            Schedule schedule3 = scheduleBean.create("Futebol Juniores Manhã Quarta", 3, "09:30", "12:00", rank1.getCode());
            rankBean.associateCoach(rank1.getCode(), coach.getUsername());
            rankBean.associateCoach(rank2.getCode(), coach.getUsername());
            rankBean.associateCoach(rank3.getCode(), coach2.getUsername());
            rankBean.associateCoach(rank3.getCode(), coach.getUsername());
            gradeBean.addAthlete(grade.getCode(), athlete.getUsername());
            gradeBean.addAthlete(grade3.getCode(), athlete.getUsername());
            gradeBean.addAthlete(grade2.getCode(), athlete2.getUsername());
            //endregion
        } catch (Exception e) {
            logger.warning("ERROR: " + e.getMessage());
        }
    }
}
