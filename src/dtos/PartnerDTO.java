package dtos;

import java.io.Serializable;

public class PartnerDTO extends UserDTO implements Serializable {
    public PartnerDTO() {
    }

    public PartnerDTO(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}