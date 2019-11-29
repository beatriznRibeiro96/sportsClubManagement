package dto;


import entities.Category;
import entities.Product;

import java.io.Serializable;
import java.util.*;

public class CategoryDTO implements Serializable {


    private int id;
    private String description;
    private  boolean invalid;

    public CategoryDTO() {
    }

    public CategoryDTO(int id, String description, boolean invalid) {
        this();
        this.id = id;
        this.description = description;
        this.invalid = invalid;
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

}
