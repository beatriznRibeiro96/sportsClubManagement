package dtos;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private String username;
    private String password;
    private String name;
    private String email;
    private String birthDate;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String name, String email, String birthDate) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }
    public void reset() {
        setUsername(null);
        setPassword(null);
        setName(null);
        setEmail(null);
        setBirthDate(null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

}
