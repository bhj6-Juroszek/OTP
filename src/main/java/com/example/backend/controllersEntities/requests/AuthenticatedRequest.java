package com.example.backend.controllersEntities.requests;

public abstract class AuthenticatedRequest implements Request {
  private String uuid;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(final String uuid) {
    this.uuid = uuid;
  }
}
