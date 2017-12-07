package com.example.backend.controllersEntities.responses;

import com.example.backend.contexts.UserContext;

import java.util.UUID;

public class LoginResponse extends Response{

  private UserContext userContext;
  private String uuid;

  public LoginResponse(final UserContext userContext) {
    this.userContext = userContext;
    this.uuid = UUID.randomUUID().toString();
  }

  public UserContext getUserContext() {
    return userContext;
  }

  public void setUserContext(final UserContext userContext) {
    this.userContext = userContext;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(final String uuid) {
    this.uuid = uuid;
  }

  public LoginResponse() {
    this.uuid = UUID.randomUUID().toString();
  }
}
