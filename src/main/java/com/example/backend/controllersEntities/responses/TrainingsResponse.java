package com.example.backend.controllersEntities.responses;

import com.example.daoLayer.entities.Training;

import java.util.List;

public class TrainingsResponse extends ResponseWithCode {

  private List<Training> userTrainings;

  public List<Training> getUserTrainings() {
    return userTrainings;
  }

  public void setUserTrainings(final List<Training> userTrainings) {
    this.userTrainings = userTrainings;
  }
}
