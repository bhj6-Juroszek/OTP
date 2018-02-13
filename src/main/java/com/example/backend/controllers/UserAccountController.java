package com.example.backend.controllers;

import com.example.backend.controllersEntities.responses.TrainingsResponse;
import com.example.backend.services.TrainingsService;
import com.example.backend.services.UsersService;
import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Nonnull;
import java.util.Date;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;
import static com.example.utils.ResponseCode.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping("/")
public class UserAccountController extends AuthenticatedController {

  private static final Logger LOGGER = LogManager.getLogger(UserAccountController.class);
  private UsersService usersService;
  private TrainingsService trainingsService;

  @RequestMapping(value = "/details", method = POST, consumes = "application/json")
  public @ResponseBody
  int changeDetails(@RequestParam("uuid") final String uuid, @RequestBody final User user) {
    if (authenticate(uuid)) {
      return usersService.updateDetails(user);
    }
    return NOT_AUTHENTICATED;
  }

  @RequestMapping(value = "/trainings", method = GET)
  public @ResponseBody
  TrainingsResponse getUserTrainings(@RequestParam("uuid") final String uuid) {
    final TrainingsResponse response = new TrainingsResponse();
    if (authenticate(uuid)) {
      response.setResponseCode(SUCCESS);
      response.setUserTrainings(trainingsService.getUserOwnedTrainings(uuid));
    } else {
      response.setResponseCode(NOT_AUTHENTICATED);
    }
    return response;
  }

  @RequestMapping(value = "/trainingTemplate", method = PUT)
  public @ResponseBody
  int updateTrainingTemplate(@RequestParam("uuid") final String uuid, @RequestParam("priceForHour") final double price,
      @RequestParam("description") final String description, @RequestParam("details") final String details,
      @RequestParam("trainingId") final String trainingId) {
    if (authenticate(uuid)) {
      trainingsService.updateTrainingTemplate(uuid, price, description, details, trainingId);
      return SUCCESS;
    }
    return NOT_AUTHENTICATED;
  }

  @RequestMapping(value = "/trainingTemplate", method = POST)
  public @ResponseBody
  int saveTrainingTemplate(@RequestParam("uuid") final String uuid, @RequestParam("placeName") final String place,
      @RequestParam("categoryId") final String categoryId, @RequestParam("price") final Double price,
      @RequestParam("description") final String description, @RequestParam("details") final String details,
      @RequestParam("capacity") final int capacity) {
    if (authenticate(uuid)) {
      final User user = manager.getLoggedUsers().get(uuid).getUser();
      final Training training = new Training();
      training.setOwner(user);
      training.setDetails(details);
      training.setPrice(price);
      training.setCapacity(capacity);
      training.setDescription(description);
      return trainingsService.saveTrainingTemplate(training, place, categoryId);
    }
    return NOT_AUTHENTICATED;
  }

  @RequestMapping(value = "/trainingInstance", method = POST)
  public @ResponseBody
  int saveTrainingInstance(@RequestParam("uuid") final String uuid, @RequestParam("date") final long date,
      @RequestParam("trainingTemplate") final String templateId, @RequestParam("duration") final double duration) {
    if (authenticate(uuid)) {
      final Date startDate = new Date(date);
      trainingsService.saveTrainingInstance(templateId, startDate, duration);
      return SUCCESS;
    }
    return NOT_AUTHENTICATED;
  }

  @CrossOrigin
  @RequestMapping(value = "/file", method = POST)
  @ResponseBody
  public int fileUpload(final MultipartHttpServletRequest mRequest) {
    final String uuid = mRequest.getParameter("uuid");
    if (authenticate(uuid)) {
      return trainingsService.addMaterials(mRequest, uuid);
    }
    return NOT_AUTHENTICATED;
  }

  @RequestMapping(value = "/unconfirmedReservations", method = GET)
  @ResponseBody
  public TrainingsResponse getUnconfirmedReservations(@RequestParam("uuid") @Nonnull final String uuid) {

    final TrainingsResponse response = new TrainingsResponse();
    response.setResponseCode(NOT_AUTHENTICATED);
    if (authenticate(uuid)) {
      response.setResponseCode(SUCCESS);
      response.setUserTrainings(trainingsService.getUnconfirmedReservations(uuid));
    }
    return response;
  }

  @RequestMapping(value = "/reservation/confirmation", method = POST)
  @ResponseBody
  public int getUnconfirmedReservatios(@RequestParam("uuid") @Nonnull final String uuid,
      @RequestParam("reservationId") @Nonnull final String reservationId) {
    if (authenticate(uuid)) {
     trainingsService.confirmReservation(uuid, reservationId);
     return SUCCESS;
    }
    return NOT_AUTHENTICATED;
  }

  @Autowired
  public void setUsersService(@Nonnull final UsersService usersService) {
    this.usersService = usersService;
  }

  @Autowired
  public void setTrainingsService(@Nonnull final TrainingsService trainingsService) {
    this.trainingsService = trainingsService;
  }
}
