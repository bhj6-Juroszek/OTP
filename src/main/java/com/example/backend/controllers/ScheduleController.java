package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.BookingRequest;
import com.example.backend.controllersEntities.responses.BookingResponse;
import com.example.backend.controllersEntities.responses.ScheduleResponse;
import com.example.backend.services.TrainingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.Date;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;
import static com.example.utils.ResponseCode.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping("/schedule")
public class ScheduleController extends AuthenticatedController {

  private TrainingsService trainingsService;

  @RequestMapping(value = "/getUserSchedule", method = GET)
  public @ResponseBody
  ScheduleResponse getUserTrainings(@RequestParam("uuid") final String uuid,
      @RequestParam("trainerId") final String trainerId, @RequestParam("date") final long date) {
    final ScheduleResponse response = new ScheduleResponse();
    if (authenticate(uuid)) {
      response.setResponseCode(SUCCESS);
      final Date scheduleWeekDate = new Date(date);
      return trainingsService.resolveScheduleResponse(uuid, response, trainerId, scheduleWeekDate);
    } else {
      response.setResponseCode(NOT_AUTHENTICATED);
    }
    return response;
  }

  @RequestMapping(value = "/removeInstance", method = DELETE)
  public @ResponseBody
  boolean removeTrainingInstance(@RequestParam("uuid") final String uuid,
      @RequestParam("instanceId") final String instanceId) {
    if (authenticate(uuid)) {
      return trainingsService.removeTrainingInstance(manager.getLoggedUsers().get(uuid).getUser(), instanceId);
    }
    return false;
  }

  @RequestMapping(value = "/bookTraining", method = POST)
  public @ResponseBody
  BookingResponse bookTraining(@RequestBody BookingRequest bookingRequest) {
    if (authenticate(bookingRequest.getUuid())) {
      return trainingsService.bookTraining(bookingRequest);
    }
    final BookingResponse response = new BookingResponse();
    response.setResponseCode(NOT_AUTHENTICATED);
    return response;
  }

  @Autowired
  public void setTrainingsService(@Nonnull final TrainingsService trainingsService) {
    this.trainingsService = trainingsService;
  }
}
