package com.example.backend.contexts;

import com.example.daoLayer.entities.User;

public class UserContext {

  private User user;
  private long lastTouched;

  public UserContext() {
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public long getLastTouched() {
    return lastTouched;
  }

  public void setLastTouched(final long lastTouched) {
    this.lastTouched = lastTouched;
  }

  public UserContext(final User user, final long lastTouched) {
    this.user = user;
    this.lastTouched = lastTouched;
  }
}
