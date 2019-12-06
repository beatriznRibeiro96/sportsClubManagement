package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "LINE_ITEMS_ORDER")
@NamedQueries(
        @NamedQuery(
                name = "getAllItems",
                query = "SELECT l FROM LineItemOrder l ORDER BY l.product.description"
        )
)
public class LineItemOrder {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    @NotNull
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    @NotNull
    private Order order;

    @NotNull
    private int quantity;

    @NotNull
    private double priceQuantity;

    public LineItemOrder() {

    }

    public LineItemOrder(Product product, Order order, int quantity) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.priceQuantity = product.getPrice() * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
