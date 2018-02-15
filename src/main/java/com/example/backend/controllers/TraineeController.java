package com.example.backend.controllers;

import com.example.backend.controllersEntities.responses.MaterialsResponse;
import com.example.backend.controllersEntities.responses.TrainingsResponse;
import com.example.backend.services.RatesService;
import com.example.backend.services.TrainingsService;
import com.example.daoLayer.entities.Rate;
import com.example.daoLayer.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;
import static com.example.utils.ResponseCode.SUCCESS;
import static java.io.File.*;
import static org.apache.commons.io.IOUtils.copy;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/trainee")
public class TraineeController extends AuthenticatedController {

  private static final Logger LOGGER = LogManager.getLogger(TraineeController.class);

  private RatesService ratesService;
  private TrainingsService trainingsService;

  @RequestMapping(value = "/rate", method = GET)
  @ResponseBody
  public TrainingsResponse getTrainingsToRate(@RequestParam("uuid") final String uuid) {
    if (authenticate(uuid)) {
      return ratesService.getTrainingsToRate(uuid);
    } else {
      final TrainingsResponse response = new TrainingsResponse();
      response.setResponseCode(NOT_AUTHENTICATED);
      return response;
    }
  }

  @RequestMapping(value = "/trainings/upcoming", method = GET)
  @ResponseBody
  public TrainingsResponse getUpcomingTrainings(@RequestParam("uuid") final String uuid) {
    if (authenticate(uuid)) {
      return trainingsService.getUpcomingTrainingsForUser(uuid);
    } else {
      final TrainingsResponse response = new TrainingsResponse();
      response.setResponseCode(NOT_AUTHENTICATED);
      return response;
    }
  }

  @RequestMapping(value = "/materials", method = GET)
  @ResponseBody
  public MaterialsResponse getMaterials(@RequestParam("uuid") final String uuid) {
    final MaterialsResponse response = new MaterialsResponse();
    response.setResponseCode(NOT_AUTHENTICATED);
    if (authenticate(uuid)) {
      response.setResponseCode(SUCCESS);
      response.setMaterialsPaths(trainingsService.getMaterials(uuid));
    }

    return response;

  }

  @RequestMapping(value = "/rate", method = POST)
  @ResponseBody
  public int getTrainingsToRate(@RequestParam("uuid") final String uuid, @RequestBody Rate rate) {
    if (authenticate(uuid)) {
      return ratesService.rate(rate, uuid);
    }
    return NOT_AUTHENTICATED;
  }

  @RequestMapping(value = "/file", method = GET)
  public void downloadFile(@RequestParam("uuid") final String uuid, @RequestParam("filePath") final String filePath,
      final HttpServletResponse response) {
    if (authenticate(uuid)) {
      try {
        final String userId = manager.getLoggedUsers().get(uuid).getUser().getId();
        final InputStream inputStream = new FileInputStream(new File(separator + userId + separator + filePath));
        copy(inputStream, response.getOutputStream());
      } catch (Exception e) {
        LOGGER.error("Error occured while requesting for a file:{}", filePath, e);
      }
    }
  }

  @Autowired
  public void setRatesService(@Nonnull final RatesService ratesService) {
    this.ratesService = ratesService;
  }

  @Autowired
  public void setTrainingsService(@Nonnull final TrainingsService trainingsService) {
    this.trainingsService = trainingsService;
  }
}
