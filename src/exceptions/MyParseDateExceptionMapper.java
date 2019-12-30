package exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class MyParseDateExceptionMapper implements ExceptionMapper<MyParseDateException> {
    @Override
    public Response toResponse(MyParseDateException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
