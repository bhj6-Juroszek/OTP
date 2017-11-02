package com.example.daoLayer.entities;

import com.example.daoLayer.DAOHandler;

import java.sql.Date;

/**
 * Created by Bartek on 2017-03-23.
 */
public class Training {

  private long id;
  private Date date;
  private Double length;
  private long price;
  private long category;
  private String city;
  private String description;
  private long ownerId;
  private long takenById;

  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(final long price) {
    this.price = price;
  }

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }

  public Double getLength() {
    return length;
  }

  public void setLength(final Double length) {
    this.length = length;
  }

  public long getCategory() {
    return category;
  }

  public void setCategory(final long category) {
    this.category = category;
  }

  public long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(final long ownerId) {
    this.ownerId = ownerId;
  }

  public long getTakenById() {
    return takenById;
  }

  public void setTakenById(final long takenById) {
    this.takenById = takenById;
  }

  public Training() {

  }

  public User getOwner() {
    return DAOHandler.usersDAO.getCustomerByProfile(ownerId);
  }
}
