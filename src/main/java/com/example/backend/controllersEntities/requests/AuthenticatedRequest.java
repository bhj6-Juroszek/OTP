package com.example.backend.controllersEntities.requests;

import java.util.UUID;

public abstract class AuthenticatedRequest implements Request {

  private String uuid;

  public AuthenticatedRequest() {
    this.uuid = UUID.randomUUID().toString();
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(final String uuid) {
    this.uuid = uuid;
  }
}
