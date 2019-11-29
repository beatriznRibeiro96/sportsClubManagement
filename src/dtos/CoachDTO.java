package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CoachDTO extends UserDTO implements Serializable {
    private Collection<SportDTO> sports;
    public CoachDTO() {
        this.sports = new LinkedHashSet<>();
    }

    public CoachDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.sports = new LinkedHashSet<>();
    }

    public Collection<SportDTO> getSports() {
        return sports;
    }

    public void setSports(Collection<SportDTO> sports) {
        this.sports = sports;
    }
}
