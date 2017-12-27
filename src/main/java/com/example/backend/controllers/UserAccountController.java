package com.example.backend.controllers;

import com.example.backend.controllersEntities.responses.UserTrainingsResponse;
import com.example.backend.model.TrainingManager;
import com.example.backend.model.UserManager;
import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;

import java.util.Date;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;
import static com.example.utils.ResponseCode.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/")
public class UserAccountController extends AuthenticatedController {

  private UserManager userManager;
  private TrainingManager trainingManager;

  @RequestMapping(value = "/changeDetails", method = POST, consumes = "application/json")
  public @ResponseBody
  int changeDetails(@RequestParam("uuid") final String uuid, @RequestBody final User user) {
    if (authenticate(uuid)) {
      return userManager.updateDetails(user);
    }
    return NOT_AUTHENTICATED;
  }

  @RequestMapping(value = "/getUserTrainings", method = GET)
  public @ResponseBody
  UserTrainingsResponse getUserTrainings(@RequestParam("uuid") final String uuid) {
    final UserTrainingsResponse response = new UserTrainingsResponse();
    if (authenticate(uuid)) {
      response.setResponseCode(SUCCESS);
      response.setUserTrainings(trainingManager.getUserTrainings(uuid));
    } else {
      response.setResponseCode(NOT_AUTHENTICATED);
    }
    return response;
  }

  @RequestMapping(value = "/saveTrainingTemplate", method = POST)
  public @ResponseBody
  int saveTrainingTemplate(@RequestParam("uuid") final String uuid, @RequestParam("placeName") final String place,
      @RequestParam("categoryId") final String categoryId, @RequestParam("price") final Double price,
      @RequestParam("description") final String description, @RequestParam("capacity") final int capacity) {
    if (authenticate(uuid)) {
      final User user = manager.getLoggedUsers().get(uuid).getUser();
      final Training training = new Training();
      training.setOwner(user);
      training.setPrice(price);
      training.setCapacity(capacity);
      training.setDescription(description);
      return trainingManager.saveTrainingTemplate(training, place, categoryId);
    }
    return NOT_AUTHENTICATED;
  }

  @RequestMapping(value = "/saveTrainingInstance", method = POST)
  public @ResponseBody
  int saveTrainingInstance(@RequestParam("uuid") final String uuid, @RequestParam("date") final long date,
      @RequestParam("trainingTemplate") final String templateId, @RequestParam("duration") final double duration) {
    if (authenticate(uuid)) {
      final Date startDate = new Date(date);
      trainingManager.saveTrainingInstance(templateId, startDate, duration);
      return SUCCESS;
    }
    return NOT_AUTHENTICATED;
  }

  @Autowired
  public void setUserManager(@Nonnull final UserManager userManager) {
    this.userManager = userManager;
  }

  @Autowired
  public void setTrainingManager(@Nonnull final TrainingManager trainingManager) {
    this.trainingManager = trainingManager;
  }
}
