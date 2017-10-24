package com.example.backend.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.*;
import static java.util.concurrent.TimeUnit.*;

@Component
public class SessionManager {

  Map<String, Long> loggedUsers;
  ScheduledExecutorService scheduler;

  public SessionManager() {
    loggedUsers = new ConcurrentHashMap<>();
    scheduler = new ScheduledThreadPoolExecutor(1);
    scheduler.scheduleAtFixedRate(this::removeUnactiveSessions, 15, 15, MINUTES);
  }

  public void addToMap(final String uuid) {
    loggedUsers.putIfAbsent(uuid, currentTimeMillis());
  }

  public Map<String, Long> getLoggedUsers() {
    return loggedUsers;
  }

  private void removeUnactiveSessions() {
    for (String uuid : loggedUsers.keySet()) {
      if ((loggedUsers.get(uuid) + MINUTES.toMillis(25L)) < currentTimeMillis()) {
        loggedUsers.remove(uuid);
      }
    }
  }
}
