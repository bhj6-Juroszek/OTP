package com.example.backend.controllers;

import com.example.backend.controllersEntities.responses.ScheduleResponse;
import com.example.backend.helpers.TrainingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nonnull;
import java.util.Date;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;
import static com.example.utils.ResponseCode.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@Controller
@RequestMapping("/schedule")
public class ScheduleController extends AuthenticatedController {

  private TrainingManager trainingManager;

  @RequestMapping(value = "/getUserSchedule", method = GET)
  public @ResponseBody
  ScheduleResponse getUserTrainings(@RequestParam("uuid") final String uuid,
      @RequestParam("trainerId") final String trainerId, @RequestParam("date") final long date) {
    final ScheduleResponse response = new ScheduleResponse();
    if (authenticate(uuid)) {
      response.setResponseCode(SUCCESS);
      final Date scheduleWeekDate = new Date(date);
      return trainingManager.resolveScheduleResponse(response, trainerId, scheduleWeekDate);
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
      return trainingManager.removeTrainingInstance(manager.getLoggedUsers().get(uuid).getUser(), instanceId);
    }
    return false;
  }

  @Autowired
  public void setTrainingManager(@Nonnull final TrainingManager trainingManager) {
    this.trainingManager = trainingManager;
  }
}
