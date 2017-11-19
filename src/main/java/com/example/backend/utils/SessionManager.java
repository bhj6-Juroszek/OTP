package com.example.backend.utils;

import com.example.backend.contexts.UserContext;
import com.example.daoLayer.entities.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.lang.System.*;
import static java.util.concurrent.TimeUnit.*;

@Component
public class SessionManager {

  Map<String, UserContext> loggedUsers;
  ScheduledExecutorService scheduler;

  public SessionManager() {
    loggedUsers = new ConcurrentHashMap<>();
    scheduler = new ScheduledThreadPoolExecutor(1);
    scheduler.scheduleAtFixedRate(this::removeUnactiveSessions, 15, 15, MINUTES);
  }

  public boolean addToMap(final String uuid, User user) {
    if(loggedUsers.containsValue(new UserContext(user, currentTimeMillis())))
    {
      return true;
    }
    loggedUsers.putIfAbsent(uuid, new UserContext(user, currentTimeMillis()));
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
