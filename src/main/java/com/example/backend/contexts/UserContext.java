package com.example.backend.contexts;

import com.example.daoLayer.entities.User;

public class UserContext {

  private long lastTouched;
  private User user;

  public UserContext() {
    this.user = new User();
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

  @Override
  public String toString() {
    return String.format("User context for user: %s, id: %d", user.getMail(), user.getId());
  }

  @Override
  public int hashCode() {
    return (int)user.getId();
  }

  @Override
  public boolean equals(Object o) {
    if(o != null) {
      if (o instanceof UserContext) {
        final UserContext userContext = (UserContext)o;
        if(this.getUser().equals(userContext.getUser())) {
          return true;
        }
      }
    }
    return false;
  }
}
