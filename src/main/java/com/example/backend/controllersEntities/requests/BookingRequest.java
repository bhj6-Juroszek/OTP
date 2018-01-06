package com.example.backend.controllersEntities.requests;

import com.example.daoLayer.entities.TrainingReservation;

public class BookingRequest extends AuthenticatedRequest {
  private TrainingReservation trainingReservation;
  private String trainingId;

  public TrainingReservation getTrainingReservation() {
    return trainingReservation;
  }

  public void setTrainingReservation(final TrainingReservation trainingReservation) {
    this.trainingReservation = trainingReservation;
  }

  public String getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(final String trainingId) {
    this.trainingId = trainingId;
  }
}
