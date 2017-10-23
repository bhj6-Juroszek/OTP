package com.example.controllers.controllersEntities;

public class RegisterRequest {
  private String email;
  private String password;

  public RegisterRequest() {
  }

  public RegisterRequest(final String email, final String password) {
    this.email = email;
    this.password = password;
  }

  public void setEmail(final String email) {

    this.email = email;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getEmail() {

    return email;
  }

  public String getPassword() {
    return password;
  }
}
