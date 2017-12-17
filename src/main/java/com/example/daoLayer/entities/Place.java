package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Bartek on 2017-05-04.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place extends Entity {

  private String name;
  private double lat;
  private double lng;

  public Place() {
    this("", 0, 0);
  }

  public Place(final String name, final double lat, final double lng) {
    super();
    this.name = name;
    this.lat = lat;
    this.lng = lng;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(final double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(final double lng) {
    this.lng = lng;
  }

  @Override
  public String toString() {
    return String.format("Place:[%s]", name);
  }
}
