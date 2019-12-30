package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CoachDTO extends UserDTO implements Serializable {
    private Collection<RankDTO> ranks;
    public CoachDTO() {
        this.ranks = new LinkedHashSet<>();
    }

    public CoachDTO(String username, String password, String name, String email, String birthDate) {
        super(username, password, name, email, birthDate);
        this.ranks = new LinkedHashSet<>();
    }

    public Collection<RankDTO> getRanks() {
        return ranks;
    }

    public void setRanks(Collection<RankDTO> ranks) {
        this.ranks = ranks;
    }
}
