package entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Table(name ="USERS", uniqueConstraints = @UniqueConstraint(columnNames = {"EMAIL"}))
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements Serializable {
    @Id
    @NotBlank(message = "username is mandatory")
    protected String username;

    @NotBlank(message = "password is mandatory")
    @Column(nullable = false)
    protected String password;

    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    protected String name;

    @Email
    @NotBlank(message = "email is mandatory")
    @Column(nullable = false)
    protected String email;

    @NotNull(message = "birth date is mandatory")
    @Column(nullable = false)
    @Past(message = "birth date needs to be before today")
    protected LocalDate birthDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Order> orders;

    @Version
    private int version;


    public User() {
        orders = new LinkedHashSet<>();
    }

    public User(String username, String password, String name, String email, LocalDate birthDate) {
        this();
        this.username = username;
        this.password = hashPassword(password);
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
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
        this.password = hashPassword(password);
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

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int age(){
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public static String hashPassword(String password) {
        if(password == null || password.equals(""))
            return null;
        char[] encoded = null;
        try {
            ByteBuffer passwdBuffer =
                    Charset.defaultCharset().encode(CharBuffer.wrap(password));
            byte[] passwdBytes = passwdBuffer.array();
            MessageDigest mdEnc = MessageDigest.getInstance("SHA-256");
            mdEnc.update(passwdBytes, 0, password.toCharArray().length);
            encoded = new BigInteger(1, mdEnc.digest()).toString(16).toCharArray();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String(encoded);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
    }

}
