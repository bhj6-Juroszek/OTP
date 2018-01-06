package com.example.backend.controllersEntities.requests;

import com.example.daoLayer.entities.Place;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainingsWithFilterRequest implements Request {
  @JsonProperty(required = false)
  private Place city;
  @JsonProperty(required = false)
  private int range;
  @JsonProperty(required = false)
  private String categoryId;
  @JsonProperty(required = false)
  private Date dateFirst;
  @JsonProperty(required = false)
  private Date dateLast;
  @JsonProperty(required = false)
  private double maxPrice;
  @JsonProperty(required = false)
  private String sortBy;
  @JsonProperty(required = false)
  private boolean showOnline;

  public boolean getShowOnline() {
    return showOnline;
  }

  public void setShowOnline(final boolean showOnline) {
    this.showOnline = showOnline;
  }

  public Place getCity() {
    return city;
  }

  public void setCity(final Place city) {
    this.city = city;
  }

  public int getRange() {
    return range;
  }

  public void setRange(final int range) {
    this.range = range;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(final String categoryId) {
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
