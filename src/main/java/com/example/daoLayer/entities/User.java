package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Entity {

  private String name;
  private String adress;
  private String mail;
  private String login;
  private String password;
  private boolean role;
  private String imageUrl;
  private String confirmation;

  public User() {
    this("", "", "", "", "", false, "", "");
  }

  public User(final String name, final String adress, final String mail, final String login, final String password,
      final boolean role, final String imageUrl, final String confirmation) {
    super();
    this.name = name;
    this.adress = adress;
    this.mail = mail;
    this.login = login;
    this.password = password;
    this.role = role;
    this.imageUrl = imageUrl;
    this.confirmation = confirmation;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public User setImageUrl(final String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  public boolean isRole() {
    return role;
  }

  public String getConfirmation() {
    return confirmation;
  }

  public void setConfirmation(final String confirmation) {
    this.confirmation = confirmation;
  }

  public String getName() {
    return name;
  }

  public User setName(String name) {
    this.name = name;
    return this;
  }

  public String getAdress() {
    return adress;
  }

  public User setAdress(String adress) {
    this.adress = adress;
    return this;
  }

  public String getMail() {
    return mail;
  }

  public User setMail(String mail) {
    this.mail = mail;
    return this;
  }

  public String getLogin() {
    return login;
  }

  public User setLogin(String login) {
    this.login = login;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public User setPassword(final String password) {
    this.password = password;
    return this;
  }

  public boolean getRole() {
    return role;
  }

  public void setRole(boolean role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object instanceof User) {
      final User user = (User) object;
      if (Objects.equals(this.id, user.getId())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return (name + " Adress:" + adress + " Mail: " + mail);
  }
}
