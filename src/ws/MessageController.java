package ws;

import dtos.MessageDTO;
import ejbs.MessageBean;
import entities.Message;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/messages") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class MessageController {
    @EJB
    private MessageBean messageBean;
    @Context
    private SecurityContext securityContext;

    public static MessageDTO toDTO(Message message){
        return new MessageDTO(
                message.getCode(),
                message.getSubject(),
                message.getBody()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<MessageDTO> toDTOs(Collection<Message> messages){
        return messages.stream().map(MessageController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/messages/”
    public Response all() {
        return Response.status(200).entity(toDTOs(messageBean.all())).build();
    }
}
