package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="MESSAGES")
@NamedQueries({
        @NamedQuery(
                name = "getAllMessages",
                query = "SELECT messa FROM Message messa ORDER BY messa.subject"
        )
})
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String body;
    @ManyToMany
    @JoinTable(name = "MESSAGES_USERS",
            joinColumns = @JoinColumn(name = "MESSAGE_CODE", referencedColumnName = "CODE", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "USER_USERNAME", referencedColumnName =
                    "USERNAME", nullable = false))
    private Set<User> users;
    @Version
    private int version;

    public Message() {
        this.users = new LinkedHashSet<>();
    }

    public Message(String subject, String body) {
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user){
        users.remove(user);
    }
}
