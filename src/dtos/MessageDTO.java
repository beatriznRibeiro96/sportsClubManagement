package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class MessageDTO implements Serializable {
    private int code;
    private String subject;
    private String body;
    private Collection<UserDTO> users;

    public MessageDTO() {
        this.users = new LinkedHashSet<>();
    }

    public MessageDTO(int code, String subject, String body) {
        this.code = code;
        this.subject = subject;
        this.body = body;
        this.users = new LinkedHashSet<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Collection<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserDTO> users) {
        this.users = users;
    }
}
