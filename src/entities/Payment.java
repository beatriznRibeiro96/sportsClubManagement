package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PAYMENTS")
public class Payment {

    @Id
    @GeneratedValue
    private int id;

    //TODO: Codigo Recibo
    //@NotNull
    //private String code; // RECIBO CODIGO - "FTR" + YEAR + "/" + ID_ORDER (String.format("%04d", this.id));

    @NotNull
    private double amount;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    @NotNull
    private Order order;

    @ManyToOne
    @JoinColumn(name = "METHODPAYMENT_ID")
    @NotNull
    private MethodPayment methodPayment;

    public Payment() {

    }

    public Payment(double amount, Order order, MethodPayment methodPayment) {
        this.amount = amount;
        this.order = order;
        this.methodPayment = methodPayment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MethodPayment getMethodPayment() {
        return methodPayment;
    }

    public void setMethodPayment(MethodPayment methodPayment) {
        this.methodPayment = methodPayment;
    }
}
