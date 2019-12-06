package dtos;

import java.io.Serializable;

public class AthleteDTO extends PartnerDTO implements Serializable {

    public AthleteDTO() {
    }

    public AthleteDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
