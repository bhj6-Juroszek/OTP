package com.example.backend.controllersEntities.requests;

public class BookingRequest extends AuthenticatedRequest {
  private String customerId;
  private String trainingId;

  public String getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(final String trainingId) {
    this.trainingId = trainingId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(final String customerId) {
    this.customerId = customerId;
  }
}
