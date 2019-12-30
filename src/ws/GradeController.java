package ws;

import dtos.GradeDTO;
import ejbs.GradeBean;
import entities.Grade;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/grades") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class GradeController {
    @EJB
    private GradeBean gradeBean;
    @Context
    private SecurityContext securityContext;

    public static GradeDTO toDTO(Grade grade){
        return new GradeDTO(
                grade.getCode(),
                grade.getName(),
                grade.getActiveSport().getCode(),
                grade.getActiveSport().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<GradeDTO> toDTOs(Collection<Grade> grades){
        return grades.stream().map(GradeController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/grades/”
    public Response all() {
        return Response.status(200).entity(toDTOs(gradeBean.all())).build();
    }

    @GET
    @Path("{code}")
    public Response getGradeDetails(@PathParam("code") int code) {
        if(securityContext.isUserInRole("Administrator")){
            String msg;
            try {
                Grade grade = gradeBean.find(code);
                if (grade != null) {
                    return Response.status(Response.Status.OK)
                            .entity(toDTO(grade))
                            .build();
                }
                msg = "ERROR_FINDING_GRADE";
                System.err.println(msg);
            } catch (Exception e) {
                msg = "ERROR_FETCHING_GRADE_DETAILS --->" + e.getMessage();
                System.err.println(msg);
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @POST
    @Path("/")
    public Response createNewGrade (GradeDTO gradeDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Grade grade = gradeBean.create(gradeDTO.getName(),
                gradeDTO.getActiveSportCode());
        return Response.status(Response.Status.CREATED).entity(toDTO(grade)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateGrade(@PathParam("code") int code, GradeDTO gradeDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        if(securityContext.isUserInRole("Administrator")) {
            Grade grade = gradeBean.update(code,
                    gradeDTO.getName(),
                    gradeDTO.getActiveSportCode());
            return Response.status(Response.Status.OK).entity(toDTO(grade)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Cannot access this information").build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteGrade (@PathParam("code") int code) throws MyEntityNotFoundException{
        gradeBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
}
