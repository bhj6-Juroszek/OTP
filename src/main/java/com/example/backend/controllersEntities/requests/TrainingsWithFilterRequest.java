package com.example.backend.controllersEntities.requests;

import java.util.Date;

public class TrainingsWithFilterRequest implements Request {

  private String cityName;
  private double range;
  private long categoryId;
  private Date dateFirst;
  private Date dateLast;
  private double maxPrice;
  private String sortBy;

  public String getCityName() {
    return cityName;
  }

  public void setCityName(final String cityName) {
    this.cityName = cityName;
  }

  public double getRange() {
    return range;
  }

  public void setRange(final double range) {
    this.range = range;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(final long categoryId) {
    this.categoryId = categoryId;
  }

  public Date getDateFirst() {
    return dateFirst;
  }

  public void setDateFirst(final Date dateFirst) {
    this.dateFirst = dateFirst;
  }

  public Date getDateLast() {
    return dateLast;
  }

  public void setDateLast(final Date dateLast) {
    this.dateLast = dateLast;
  }

  public double getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(final double maxPrice) {
    this.maxPrice = maxPrice;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(final String sortBy) {
    this.sortBy = sortBy;
  }
}
