package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.TrainingsWithFilterRequest;
import com.example.backend.services.TrainingsService;
import com.example.daoLayer.daos.PlacesDAO;
import com.example.daoLayer.entities.Country;
import com.example.daoLayer.entities.Place;
import com.example.daoLayer.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/")
public class SearchController extends AuthenticatedController {

  private TrainingsService trainingsService;
  private PlacesDAO placesDAO;

  @RequestMapping(value = "/trainingsWithFilter", method = POST)
  @ResponseBody public List<Training> getTrainingsWithFilters(@RequestBody TrainingsWithFilterRequest request) {
    if(authenticate(request.getUuid())) {
      return trainingsService
          .getTrainingsByFilters(request.getCity(), request.getRange(), request.getCategoryId(),
              request.getDateFirst(), request.getDateLast(), request.getMaxPrice(), request.getSortBy(),
              request.getShowOnline());
    }
    return emptyList();
  }

  @RequestMapping(value = "/countries", method = GET)
  @ResponseBody public List<Country> getCities() {
    return placesDAO.getCountries();
  }

  @RequestMapping(value = "/cities", method = GET)
  @ResponseBody public List<Place> getCities(@RequestParam("countryCode") final String countryCode, @RequestParam(name ="prefix", required = false) final String prefix) {
    return placesDAO.getCities(countryCode, prefix);
  }

  @Autowired
  public void setTrainingsService(@Nonnull final TrainingsService trainingsService) {
    this.trainingsService = trainingsService;
  }

  @Autowired
  public void setPlacesDAO(final PlacesDAO placesDAO) {
    this.placesDAO = placesDAO;
  }

}
