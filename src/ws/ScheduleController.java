package ws;

import dtos.ScheduleDTO;
import ejbs.ScheduleBean;
import entities.Schedule;
import exceptions.*;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Path("/schedules") // relative url web path of this controller
@Produces((MediaType.APPLICATION_JSON)) // injects header “Content-Type: application/json”
@Consumes((MediaType.APPLICATION_JSON)) // injects header “Accept: application/json”
public class ScheduleController {
    @EJB
    private ScheduleBean scheduleBean;

    public static ScheduleDTO toDTO(Schedule schedule){
        return new ScheduleDTO(
                schedule.getCode(),
                schedule.getName(),
                schedule.getDayOfWeek().ordinal(),
                schedule.getDayOfWeek().name(),
                schedule.getStartTime().format(DateTimeFormatter.ofPattern("H:mm")),
                schedule.getEndTime().format(DateTimeFormatter.ofPattern("H:mm")),
                schedule.getActiveSport().getCode(),
                schedule.getActiveSport().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<ScheduleDTO> toDTOs(Collection<Schedule> schedules){
        return schedules.stream().map(ScheduleController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the verb get
    @Path("/") // means: the relative url path is “/api/ranks/”
    public Response all() {
        return Response.status(200).entity(toDTOs(scheduleBean.all())).build();
    }

    @GET
    @Path("{code}")
    public Response getScheduleDetails(@PathParam("code") int code) {
        String msg;
        try {
            Schedule schedule = scheduleBean.find(code);
            if (schedule != null) {
                return Response.status(Response.Status.OK)
                        .entity(toDTO(schedule))
                        .build();
            }
            msg = "ERROR_FINDING_SCHEDULE";
            System.err.println(msg);
        } catch (Exception e) {
            msg = "ERROR_FETCHING_SCHEDULE_DETAILS --->" + e.getMessage();
            System.err.println(msg);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @POST
    @Path("/")
    public Response createNewSchedule (ScheduleDTO scheduleDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MyParseDateException, MyIllegalArgumentException {
        Schedule schedule = scheduleBean.create(scheduleDTO.getName(),
                scheduleDTO.getDayOfWeekCode(),
                scheduleDTO.getStartTime(),
                scheduleDTO.getEndTime(),
                scheduleDTO.getActiveSportCode());
        return Response.status(Response.Status.CREATED).entity(toDTO(schedule)).build();
    }

    @PUT
    @Path("{code}")
    public Response updateSchedule(@PathParam("code") int code, ScheduleDTO scheduleDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyParseDateException, MyIllegalArgumentException {
        Schedule schedule = scheduleBean.update(code,
                scheduleDTO.getName(),
                scheduleDTO.getDayOfWeekCode(),
                scheduleDTO.getStartTime(),
                scheduleDTO.getEndTime(),
                scheduleDTO.getActiveSportCode());
        return Response.status(Response.Status.OK).entity(toDTO(schedule)).build();
    }

    @DELETE
    @Path("{code}")
    public Response deleteSchedule (@PathParam("code") int code) throws MyEntityNotFoundException{
        scheduleBean.delete(code);
        return Response.status(Response.Status.OK).build();
    }
}
