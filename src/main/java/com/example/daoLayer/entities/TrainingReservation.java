package com.example.daoLayer.entities;

public class TrainingReservation extends Entity {

  private String trainingInstance;
  private User customer;

  public TrainingReservation() {
    super();
  }

  public String getTrainingInstance() {
    return trainingInstance;
  }

  public void setTrainingInstance(final String trainingInstance) {
    this.trainingInstance = trainingInstance;
  }

  public User getCustomer() {
    return customer;
  }

  public void setCustomer(final User customer) {
    this.customer = customer;
  }
}
