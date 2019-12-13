package dtos;

import java.io.Serializable;

public class MethodPaymentDTO implements Serializable {


    private int id;
    private String method;
    private  boolean invalid;

    public MethodPaymentDTO() {

    }

    public MethodPaymentDTO(int id, String method, boolean invalid) {
        this.id = id;
        this.method = method;
        this.invalid = invalid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
}
