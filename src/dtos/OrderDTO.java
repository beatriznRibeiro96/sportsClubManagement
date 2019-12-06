package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class OrderDTO implements Serializable {


    private int id;
    //private String code; //FATURA: "FT" + YEAR + "/" + ID_ORDER (String.format("%04d", this.id));
    private double priceTotal = 0;
    private Collection<LineItemOrderDTO> lineItemOrders;
    private String username;
    private Collection<PaymentDTO> payments;
    private String status;
    private double missing;
    private  boolean invalid;

    public OrderDTO() {
        this.lineItemOrders = new LinkedHashSet<>();
        this.payments =  new LinkedHashSet<>();
    }

    public OrderDTO(int id, double priceTotal, String username, String status, double missing, boolean invalid) {
        this();
        this.id = id;
        this.priceTotal = priceTotal;
        this.username = username;
        this.status = status;
        this.missing = missing;
        this.invalid = invalid;
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

    public Collection<LineItemOrderDTO> getLineItemOrders() {
        return lineItemOrders;
    }

    public void setLineItemOrders(Collection<LineItemOrderDTO> lineItemOrders) {
        this.lineItemOrders = lineItemOrders;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(Collection<PaymentDTO> payments) {
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
}
