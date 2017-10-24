package com.example.backend.controllersEntities.responses;

public abstract class Response {

  private int responseCode;

  public int getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(final int responseCode) {
    this.responseCode = responseCode;
  }
}
