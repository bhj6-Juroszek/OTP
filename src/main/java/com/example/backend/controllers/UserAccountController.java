package com.example.backend.controllers;

import com.example.backend.model.JsonReader;
import com.example.backend.model.TrainingManager;
import com.example.backend.model.UserManager;
import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;
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

  @Autowired
  public void setUserManager(@Nonnull final UserManager userManager) {
    this.userManager = userManager;
  }

  @Autowired
  public void setTrainingManager(@Nonnull final TrainingManager trainingManager) {
    this.trainingManager = trainingManager;
  }
}
