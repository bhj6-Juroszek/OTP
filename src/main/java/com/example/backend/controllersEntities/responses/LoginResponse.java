package com.example.backend.controllersEntities.responses;

import com.example.backend.contexts.UserContext;

public class LoginResponse extends Response{

  private UserContext userContext;

  public LoginResponse(final UserContext userContext) {
    this.userContext = userContext;
  }

  public UserContext getUserContext() {
    return userContext;
  }

  public void setUserContext(final UserContext userContext) {
    this.userContext = userContext;
  }

  public LoginResponse() {
  }
}
