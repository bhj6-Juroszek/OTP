package com.example.utils;

import com.example.backend.contexts.UserContext;
import com.example.daoLayer.entities.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public class SessionManager {

  public static final String DOMAIN_NAME = "77.55.216.154:8181/frontend/";
  public static final String HOST_NAME = "77.55.216.154:8181/";

  private Map<String, UserContext> loggedUsers;

  public SessionManager() {
    loggedUsers = new ConcurrentHashMap<>();
    final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
    scheduler.scheduleAtFixedRate(this::removeUnactiveSessions, 15, 15, MINUTES);
  }

  public void removeUser(@Nonnull final User user) {
    final UserContext userContext = new UserContext(user, currentTimeMillis());
    removeUserContext(userContext);
  }

  public void removeUserContext(@Nonnull final UserContext userContext) {
    if(loggedUsers.containsValue(userContext))
    {
      loggedUsers.values().remove(userContext);
    }
  }

  public boolean addToMap(@Nonnull final String uuid, @Nonnull User user) {
    final UserContext newUserContext = new UserContext(user, currentTimeMillis());
    removeUserContext(newUserContext);
    loggedUsers.put(uuid, newUserContext);
    return true;
  }

  public Map<String, UserContext> getLoggedUsers() {
    return loggedUsers;
  }

  private void removeUnactiveSessions() {
    for (String uuid : loggedUsers.keySet()) {
      if ((loggedUsers.get(uuid).getLastTouched() + MINUTES.toMillis(25L)) < currentTimeMillis()) {
        loggedUsers.remove(uuid);
      }
    }
  }
}
