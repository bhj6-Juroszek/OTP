package com.example.utils;

import com.example.backend.contexts.UserContext;
import com.example.daoLayer.entities.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.lang.System.*;
import static java.util.concurrent.TimeUnit.*;

@Component
public class SessionManager {

  public static final String DOMAIN_NAME = "localhost:8181/executable/frontend/";
  public static final String HOST_NAME = "localhost:8181/";

  Map<String, UserContext> loggedUsers;
  ScheduledExecutorService scheduler;

  public SessionManager() {
    loggedUsers = new ConcurrentHashMap<>();
    scheduler = new ScheduledThreadPoolExecutor(1);
    scheduler.scheduleAtFixedRate(this::removeUnactiveSessions, 15, 15, MINUTES);
  }

  public boolean addToMap(@Nonnull final String uuid, @Nonnull User user) {
    final UserContext newUserContext = new UserContext(user, currentTimeMillis());
    if(loggedUsers.containsValue(newUserContext))
    {
      loggedUsers.values().remove(newUserContext);
    }
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
