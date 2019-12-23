package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CoachDTO extends UserDTO implements Serializable {
    private Collection<ActiveSportDTO> activeSports;
    public CoachDTO() {
        this.activeSports = new LinkedHashSet<>();
    }

    public CoachDTO(String username, String password, String name, String email, String birthDate) {
        super(username, password, name, email, birthDate);
        this.activeSports = new LinkedHashSet<>();
    }

    public Collection<ActiveSportDTO> getActiveSports() {
        return activeSports;
    }

    public void setActiveSports(Collection<ActiveSportDTO> activeSports) {
        this.activeSports = activeSports;
    }
}
