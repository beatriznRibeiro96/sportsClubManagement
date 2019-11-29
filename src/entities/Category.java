package entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@MappedSuperclass
@Entity
@Table(
        name = "CATEGORIES",
        uniqueConstraints = @UniqueConstraint(columnNames = {"DESCRIPTION"})
)
@NamedQueries(
        @NamedQuery(
                name = "getAllCategories",
                query = "SELECT c FROM Category c ORDER BY c.description"
        )
)
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private Set<Product> products;

    @NotNull
    private  boolean invalid = false;

    public Category() {
        this.products = new LinkedHashSet<>();
    }

    public Category(String description) {
        this();
        this.description = description;
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

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

}
