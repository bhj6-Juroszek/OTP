package com.example.backend.controllers;

import com.example.backend.controllersEntities.responses.TrainingsResponse;
import com.example.backend.services.RatesService;
import com.example.backend.services.TrainingsService;
import com.example.daoLayer.entities.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/trainee")
public class TraineeController extends AuthenticatedController{

  private RatesService ratesService;
  private TrainingsService trainingsService;

  @RequestMapping(value = "/getTrainingsToRate", method = GET)
  @ResponseBody public TrainingsResponse getTrainingsToRate(@RequestParam("uuid") final String uuid) {
    if(authenticate(uuid)) {
      return ratesService.getTrainingsToRate(uuid);
    } else {
      final TrainingsResponse response = new TrainingsResponse();
      response.setResponseCode(NOT_AUTHENTICATED);
      return response;
    }
  }

  @RequestMapping(value = "/getUpcomingTrainings", method = GET)
  @ResponseBody public TrainingsResponse getUpcomingTrainings(@RequestParam("uuid") final String uuid) {
    if(authenticate(uuid)) {
      return trainingsService.getUpcomingTrainingsForUser(uuid);
    } else {
      final TrainingsResponse response = new TrainingsResponse();
      response.setResponseCode(NOT_AUTHENTICATED);
      return response;
    }
  }

  @RequestMapping(value = "/rateTrainer", method = POST)
  @ResponseBody public int getTrainingsToRate(@RequestParam("uuid") final String uuid, @RequestBody Rate rate) {
    if(authenticate(uuid)) {
      return ratesService.rate(rate, uuid);
    }
    return NOT_AUTHENTICATED;
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
