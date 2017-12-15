package com.example.daoLayer.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartek on 2017-03-23.
 */
public class Training {

  private long id;
  private Category category;
  private Place place;
  private double price;
  private String description;
  private long capacity;
  private User owner;
  private List<TrainingInstance> instances;


  public Training() {
    this.instances = new ArrayList<>();
  }
  public long getId() {
    return id;
  }

  public List<TrainingInstance> getInstances() {
    return instances;
  }

  public void setInstances(final List<TrainingInstance> instances) {
    this.instances = instances;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(final Category category) {
    this.category = category;
  }

  public Place getPlace() {
    return place;
  }

  public void setPlace(final Place place) {
    this.place = place;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(final double price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public long getCapacity() {
    return capacity;
  }

  public void setCapacity(final long capacity) {
    this.capacity = capacity;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(final User owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "Training: id:" + this.id + ",desc:" + this.description;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o instanceof Training) {
      final Training training = (Training) o;
      return this.id == training.getId();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (int) this.id;
  }
}
