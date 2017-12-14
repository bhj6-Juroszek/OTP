package com.example.daoLayer.entities;

public class TrainingReservation {
  private long id;
  private long trainingInstance;
  private User customer;

  public long getId() {
    return id;
  }

  public long getTrainingInstance() {
    return trainingInstance;
  }

  public void setTrainingInstance(final long trainingInstance) {
    this.trainingInstance = trainingInstance;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public User getCustomer() {
    return customer;
  }

  public void setCustomer(final User customer) {
    this.customer = customer;
  }
}
