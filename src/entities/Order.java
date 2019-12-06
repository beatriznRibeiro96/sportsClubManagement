package entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ORDERS")
@NamedQueries(
        @NamedQuery(
                name = "getAllOrders",
                query = "SELECT o FROM Order o ORDER BY o.id"
        )
)
public class Order {

    @Id
    @GeneratedValue
    private int id;

    //TODO: Codigo Fatura
    //@NotNull
    //private String code; //FATURA: "FT" + YEAR + "/" + ID_ORDER (String.format("%04d", this.id));

    @NotNull
    private double priceTotal = 0;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<LineItemOrder> lineItemOrders;

    @ManyToOne
    @JoinColumn(name = "USER_USERNAME")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<Payment> payments;

    @NotNull
    private String status = "NÃO PAGO"; //PAGO - NAO PAGO - PARCIAL

    @NotNull
    private double missing = 0;

    @NotNull
    private  boolean invalid = false;

    public Order() {
        this.lineItemOrders = new LinkedHashSet<>();
        this.payments = new LinkedHashSet<>();
    }

    public Order(User user) {
        this();
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Set<LineItemOrder> getLineItemOrders() {
        return lineItemOrders;
    }

    public void setLineItemOrders(Set<LineItemOrder> lineItemOrders) {
        this.lineItemOrders = lineItemOrders;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMissing() {
        return missing;
    }

    public void setMissing(double missing) {
        this.missing = missing;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public void addItem(LineItemOrder lineItemOrder) {
        this.lineItemOrders.add(lineItemOrder);
    }

    public void removeItem(LineItemOrder lineItemOrder){
        this.lineItemOrders.remove(lineItemOrder);
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }

    public void removePayment(Payment payment) {
        this.payments.remove(payment);
    }
}
