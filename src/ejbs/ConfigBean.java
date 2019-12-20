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

    @PostConstruct
    public void PopulateDB(){
        try {
            Administrator administrator = administratorBean.create("admin1","admin","Joao","joao@mail.pt");

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
            Coach coach = coachBean.create("coach1","coach","Joana","joana@mail.pt");
            Coach coach2 = coachBean.create("coach2", "coach", "Maria", "maria@mail.pt");
            Partner partner = partnerBean.create("partner1", "partner", "Miguel", "miguel@mail.pt");
            Athlete athlete = athleteBean.create("athlete1", "athlete", "Rui", "rui@mail.pt");
            Athlete athlete2 = athleteBean.create("athlete2", "athlete", "Luís", "luis@mail.pt");
            Season season = seasonBean.create("18/19");
            Sport sport = sportBean.create("Futebol");
            Sport sport2 = sportBean.create("Andebol");
            ActiveSport activeSport = activeSportBean.create("Futebol-18/19", sport.getCode(), season.getCode());
            ActiveSport activeSport2 = activeSportBean.create("Andebol-18/19", sport2.getCode(), season.getCode());
            SportSubscription sportSubscription = sportSubscriptionBean.create("Fut18/19-Rui", activeSport.getCode(), athlete.getUsername());
            SportSubscription sportSubscription2 = sportSubscriptionBean.create("And18/19-Rui", activeSport2.getCode(), athlete.getUsername());
            SportSubscription sportSubscription3 = sportSubscriptionBean.create("And18/19-Luís", activeSport2.getCode(), athlete2.getUsername());
            Rank rank1 = rankBean.create("Juniores", 16, 19, activeSport.getCode());
            Rank rank2 = rankBean.create("Seniores", 16, 35, activeSport.getCode());
            Rank rank3 = rankBean.create("Seniores", 18, 40, activeSport2.getCode());
            activeSportBean.associateCoach(activeSport.getCode(), coach.getUsername());
            activeSportBean.associateCoach(activeSport2.getCode(), coach.getUsername());
            activeSportBean.associateCoach(activeSport.getCode(), coach2.getUsername());
            //endregion
        } catch (Exception e) {
            logger.warning("ERROR: " + e.getMessage());
        }
    }
}
