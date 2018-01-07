package com.example.backend.controllersEntities.responses;

import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.entities.User;

public class ProfileWithUserResponse {
  private Profile profile;
  private User user;

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(final Profile profile) {
    this.profile = profile;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }
}
