package dtos;

import java.io.Serializable;

public class PaymentDTO implements Serializable {


    private int id;
    //private String code; // RECIBO CODIGO - "FTR" + YEAR + "/" + ID_ORDER (String.format("%04d", this.id));
    private double amount;
    private int orderID;
    private int methodPaymentID;

    public PaymentDTO() {

    }

    public PaymentDTO(int id, double amount, int orderID, int methodPaymentID) {
        this.id = id;
        this.amount = amount;
        this.orderID = orderID;
        this.methodPaymentID = methodPaymentID;
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

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getMethodPaymentID() {
        return methodPaymentID;
    }

    public void setMethodPaymentID(int methodPaymentID) {
        this.methodPaymentID = methodPaymentID;
    }
}
