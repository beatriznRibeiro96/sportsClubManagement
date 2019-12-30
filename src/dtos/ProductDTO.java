package dtos;


import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class ProductDTO implements Serializable {


    private int id;
    private String description;
    private double price;
    private int categoryID;
    private String categoryDescription;
    private  boolean invalid;
    private int replacingProductId;
    private Collection<ProductDTO> replacedBy;

    public ProductDTO() {
        this.replacedBy = new LinkedHashSet<>();
    }

    public ProductDTO(int id, String description, double price, int categoryID, String categoryDescription, boolean invalid, int replacingProductId) {
        this();
        this.id = id;
        this.description = description;
        this.price = price;
        this.categoryID = categoryID;
        this.categoryDescription = categoryDescription;
        this.invalid = invalid;
        this.replacingProductId = replacingProductId;
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

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public int getReplacingProductId() {
        return replacingProductId;
    }

    public void setReplacingProductId(int replacingProductId) {
        this.replacingProductId = replacingProductId;
    }

    public Collection<ProductDTO> getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(Collection<ProductDTO> replacedBy) {
        this.replacedBy = replacedBy;
    }
}
