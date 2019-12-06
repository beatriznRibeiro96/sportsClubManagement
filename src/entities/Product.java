package entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@MappedSuperclass
@Entity
@Table(name = "PRODUCTS")
@NamedQueries({
        @NamedQuery(
                name = "getAllProducts",
                query = "SELECT p FROM Product p ORDER BY p.id"
        ),
        @NamedQuery(
                name = "getValidProducts",
                query = "SELECT p FROM Product p WHERE p.invalid = false AND p.category.invalid = false ORDER BY p.id"
        )
})
public class Product {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String description;

    @NotNull
    private double price;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    @NotNull
    private Category category;

    @NotNull
    private  boolean invalid = false;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = true)
    private Product replaces;

    @OneToMany
    private Set<Product> replacedBy;

    public Product() {
        replacedBy = new LinkedHashSet<>();
    }

    public Product(String description, double price, Category category) {
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public Product getReplaces() {
        return replaces;
    }

    public void setReplaces(Product replaces) {
        this.replaces = replaces;
    }

    public Set<Product> getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(Set<Product> replacedBy) {
        this.replacedBy = replacedBy;
    }

    public void addProductToReplacedBy(Product product) {
        this.replacedBy.add(product);
    }

    public void removeProductFromReplacedBy(Product product) {
        this.replacedBy.remove(product);
    }
}
