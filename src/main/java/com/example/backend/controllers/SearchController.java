package com.example.backend.controllers;

import com.example.daoLayer.daos.PlacesDAO;
import com.example.daoLayer.daos.TrainingsDAO;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.entities.Place;
import com.example.backend.model.TrainingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nonnull;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@Controller
@RequestMapping("/")
public class SearchController {

  private TrainingManager trainingManager;
  private TrainingsDAO trainingsDAO;
  private PlacesDAO placesDAO;

//  @RequestMapping(value = "/trainingsWithFilter", method = POST)
//  @ResponseBody public List<Training> getTrainingsWithFilters(@RequestBody TrainingsWithFilterRequest request) {
//    List<Training> list =  trainingManager
//        .getTrainingsByFilters(request.getCityName(), request.getRange(), request.getCategoryId(),
//            request.getDateFirst(), request.getDateLast(), request.getMaxPrice(), request.getSortBy(), true);
//    return list;
//  }

  @RequestMapping(value = "/cities", method = GET)
  @ResponseBody public List<Place> getCities() {
    return placesDAO.getAll();
  }

  @RequestMapping(value = "/test", method = GET)
  public String test() {
    final Training training = new Training();
    training.setPlace(new Place());
    training.setOwner(new User());
    training.setCategory(new Category());
//    trainingsDAO.saveTrainingAsynchronously(training);
    return "coss";
  }

  @Autowired
  public void setTrainingManager(@Nonnull final TrainingManager trainingManager) {
    this.trainingManager = trainingManager;
  }

  @Autowired
  public void setPlacesDAO(final PlacesDAO placesDAO) {
    this.placesDAO = placesDAO;
  }

  @Autowired
  public void setTrainingsDAO(final TrainingsDAO trainingsDAO) {
    this.trainingsDAO = trainingsDAO;
  }
}
