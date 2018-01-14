package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

/**
 * Created by Bartek on 2017-03-23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Training extends Entity {
  @JsonProperty(required = false)
  private Category category;
  @JsonProperty(required = false)
  private Place place;
  private double price;
  private String description;
  private String details;
  private long capacity;
  @JsonProperty(required = false)
  private User owner;
  @JsonProperty(required = false)
  private List<TrainingInstance> instances;

  public String getDetails() {
    return details;
  }

  public void setDetails(final String details) {
    this.details = details;
  }

  public Training() {
    super();
    this.instances = new ArrayList<>();
  }

  public List<TrainingInstance> getInstances() {
    if(instances == null) {
      return emptyList();
    }
    return instances;
  }

  public void setInstances(final List<TrainingInstance> instances) {
    this.instances = instances;
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
      return Objects.equals(this.id, training.getId());
    }
    return false;
  }

}
