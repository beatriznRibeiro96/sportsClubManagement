package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CoachDTO extends UserDTO implements Serializable {
    public CoachDTO() {
    }

    public CoachDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
