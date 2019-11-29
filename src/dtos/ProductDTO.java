package dto;


import entities.Product;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class ProductDTO implements Serializable {


    private int id;
    private String description;
    private double price;
    private int categoryID;
    private  boolean invalid;
    private int replacingCategoryId;
    private Collection<ProductDTO> replacedBy;

    public ProductDTO() {
        this.replacedBy = new LinkedHashSet<>();
    }

    public ProductDTO(int id, String description, double price, int categoryID, boolean invalid, int replacingCategoryId) {
        this();
        this.id = id;
        this.description = description;
        this.price = price;
        this.categoryID = categoryID;
        this.invalid = invalid;
        this.replacingCategoryId = replacingCategoryId;
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

    public int getReplacingCategoryId() {
        return replacingCategoryId;
    }

    public void setReplacingCategoryId(int replacingCategoryId) {
        this.replacingCategoryId = replacingCategoryId;
    }

    public Collection<ProductDTO> getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(Collection<ProductDTO> replacedBy) {
        this.replacedBy = replacedBy;
    }
}
