package com.example.daoLayer.entities;

public class Country {
  private String countryId;
  private String countryName;

  public String getCountryId() {
    return countryId;
  }

  public void setCountryId(final String countryId) {
    this.countryId = countryId;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(final String countryName) {
    this.countryName = countryName;
  }

  @Override
  public String toString() {
    return String.format("Country:[code:%s, name:%s]", countryId, countryName);
  }
}
