package dtos;

import java.io.Serializable;

public class LineItemOrderDTO implements Serializable {

    private int id;
    private int productID;
    private String productDescription;
    private int orderID;
    private int quantity;
    private double priceQuantity;

    public LineItemOrderDTO() {

    }

    public LineItemOrderDTO(int id, int productID, String productDescription, int orderID, int quantity, double priceQuantity) {
        this.id = id;
        this.productID = productID;
        this.productDescription = productDescription;
        this.orderID = orderID;
        this.quantity = quantity;
        this.priceQuantity = priceQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceQuantity() {
        return priceQuantity;
    }

    public void setPriceQuantity(double priceQuantity) {
        this.priceQuantity = priceQuantity;
    }
}
