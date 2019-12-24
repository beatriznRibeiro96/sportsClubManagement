package dtos;


import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class AthleteDTO extends PartnerDTO implements Serializable {
    private Collection<SportSubscriptionDTO> sportSubscriptions;
    public AthleteDTO() {
        this.sportSubscriptions = new LinkedHashSet<>();
    }

    public AthleteDTO(String username, String password, String name, String email, String birthDate) {
        super(username, password, name, email, birthDate);
        this.sportSubscriptions = new LinkedHashSet<>();
    }

    public Collection<SportSubscriptionDTO> getSportSubscriptions() {
        return sportSubscriptions;
    }

    public void setSportSubscriptions(Collection<SportSubscriptionDTO> sportSubscriptions) {
        this.sportSubscriptions = sportSubscriptions;
    }
}
