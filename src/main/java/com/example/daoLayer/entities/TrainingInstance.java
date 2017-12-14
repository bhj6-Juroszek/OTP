package com.example.daoLayer.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingInstance {
  private long id;
  private long trainingParent;
  private Date dateStart;
  private Date dateEnd;
  private List<TrainingReservation> trainingReservations;


  public TrainingInstance() {
    this.trainingReservations = new ArrayList<>();
  }

  public long getTrainingParent() {
    return trainingParent;
  }

  public Date getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(final Date dateEnd) {
    this.dateEnd = dateEnd;
  }

  public void setTrainingParent(final long trainingParent) {
    this.trainingParent = trainingParent;
  }

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
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
      return this.id == trainingInstance.getId();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (int) this.id;
  }
}
