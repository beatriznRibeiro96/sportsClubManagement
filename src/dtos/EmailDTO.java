package dtos;


import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class EmailDTO implements Serializable {
    private String subject;
    private String message;
    private Collection<UserDTO> recepientes;

    public EmailDTO() {
        this.recepientes = new LinkedHashSet<>();
    }

    public EmailDTO(String subject, String message) {
        this.recepientes = new LinkedHashSet<>();
        this.subject = subject;
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Collection<UserDTO> getRecepientes() {
        return recepientes;
    }

    public void setRecepientes(Collection<UserDTO> recepientes) {
        this.recepientes = recepientes;
    }
}
