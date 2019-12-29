package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class MyNoRecipientExceptionMapper implements ExceptionMapper<MyNoRecipientException> {
    @Override
    public Response toResponse(MyNoRecipientException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}
