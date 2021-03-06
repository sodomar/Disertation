package mainAPI.controller;

import io.swagger.annotations.*;
import mainAPI.exception.CustomException;
import mainAPI.model.Event;
import mainAPI.model.EventReservation;
import mainAPI.service.EventReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Created by cbadea on 4/3/2018.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/eventReservation")
@Api("eventReservation")
public class EventReservationController {

    private static final Logger LOGGER = (Logger) LoggerFactory.
            getLogger(EventReservationController.class);

    @Autowired
    EventReservationService eventReservationService;

    @PostMapping(value = "/addEventReservation")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${EventReservationController.addEventReservation}", response = EventReservation.class)
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<?> addEventReservation(@ApiParam("Add EventReservation") @RequestBody EventReservation eventReservation,
                                              @ApiParam("UserId") @RequestParam(value = "userId") int userId,
                                              @ApiParam("EventId") @RequestParam(value = "eventId") int eventId) {
       try {
        EventReservation eventReservationSaved = eventReservationService.addEventReservation(eventReservation, userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventId);
       } catch(CustomException e) {
           return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
       }
    }

    @DeleteMapping(value = "/deleteEventReservation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${EventReservationController.deleteReservation}", response = EventReservation.class)
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<?> deleteEventReservation(@ApiParam("EventReservationId") @RequestParam(value = "eventReservationId") int eventReservationId) {
        eventReservationService.deleteEventReservation(eventReservationId);
        LOGGER.warn("Deleted Event with id: " + eventReservationId);
        return ResponseEntity.ok().body(eventReservationId);
    }


    @GetMapping(value = "/getReservations/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${EventReservationController.getReservations}", response = EventReservation.class)
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public List<EventReservation> getReservations(@PathVariable(value = "userId") int userId) {
       return eventReservationService.getReservations(userId);
    }
}
