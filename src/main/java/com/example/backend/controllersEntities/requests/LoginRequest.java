package com.example.backend.controllersEntities.requests;

public class LoginRequest extends AuthenticatedRequest {

  private String email;
  private String password;

  public LoginRequest() {
    super();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }
}
