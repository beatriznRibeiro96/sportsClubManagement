package dtos;

import java.io.Serializable;

public class AdministratorDTO extends UserDTO implements Serializable {

    public AdministratorDTO() {
    }

    public AdministratorDTO(String username, String password, String name, String email, String birthDate) {
        super(username, password, name, email, birthDate);
    }
}
