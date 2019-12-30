package ws;

import dtos.EmailDTO;
import dtos.UserDTO;
import ejbs.EmailBean;
import exceptions.MyNoRecipientException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class UserController {
    @EJB
    private EmailBean emailBean;

    @POST
    @Path("email/send")
    public Response sendEmailToUser(EmailDTO email) throws MyNoRecipientException {
        String recepientes = "";
        int counter = 0;
        for (UserDTO userDTO:email.getRecepientes()) {
            if(counter == 0){
                recepientes = recepientes + userDTO.getEmail();
            }
            else{
                recepientes = recepientes + ", " + userDTO.getEmail();
            }
            counter++;
        }
        emailBean.send(recepientes, email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
