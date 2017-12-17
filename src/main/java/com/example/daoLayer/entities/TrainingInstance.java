package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainingInstance extends Entity {

  private String trainingParent;
  private Date dateStart;
  private Date dateEnd;
  private List<TrainingReservation> trainingReservations;

  public TrainingInstance() {
    super();
    this.trainingReservations = new ArrayList<>();
  }

  public String getTrainingParent() {
    return trainingParent;
  }

  public Date getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(final Date dateEnd) {
    this.dateEnd = dateEnd;
  }

  public void setTrainingParent(final String trainingParent) {
    this.trainingParent = trainingParent;
  }

  public Date getDateStart() {
    return dateStart;
  }

  public void setDateStart(final Date dateStart) {
    this.dateStart = dateStart;
  }

  public List<TrainingReservation> getTrainingReservations() {
    return trainingReservations;
  }

  public void setTrainingReservations(final List<TrainingReservation> trainingReservations) {
    this.trainingReservations = trainingReservations;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o instanceof TrainingInstance) {
      final TrainingInstance trainingInstance = (TrainingInstance) o;
      return Objects.equals(this.id, trainingInstance.getId());
    }
    return false;
  }
}
