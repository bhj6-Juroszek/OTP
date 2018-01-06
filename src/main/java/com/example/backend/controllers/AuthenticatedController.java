package com.example.backend.controllers;

import com.example.backend.contexts.UserContext;
import com.example.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.util.Date;

@Controller
public abstract class AuthenticatedController {
  SessionManager manager;

  @Autowired
  public void setManager(@Nonnull final SessionManager manager) {
    this.manager = manager;
  }

  boolean authenticate(@Nonnull final String uuid) {
    final UserContext userContext = manager.getLoggedUsers().get(uuid);
    if(userContext == null) {
      return false;
    }
    userContext.setLastTouched(new Date().getTime());
    return true;
  }

}
