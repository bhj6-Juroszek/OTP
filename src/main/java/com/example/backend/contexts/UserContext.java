package com.example.backend.contexts;

import com.example.daoLayer.entities.User;

public class UserContext {

  private long lastTouched;
  private User user;

  public UserContext() {
  }

  public UserContext(final User user, final long lastTouched) {
    this.lastTouched = lastTouched;
    this.user = user;
  }

  public long getLastTouched() {
    return lastTouched;
  }

  public void setLastTouched(final long lastTouched) {
    this.lastTouched = lastTouched;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }
}
