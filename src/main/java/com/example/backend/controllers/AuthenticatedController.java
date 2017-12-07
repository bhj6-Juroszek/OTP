package com.example.backend.controllers;

import com.example.backend.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;

@Controller
public abstract class AuthenticatedController {
  SessionManager manager;

  @Autowired
  public void setManager(@Nonnull final SessionManager manager) {
    this.manager = manager;
  }

  boolean authenticate(final String uuid) {
    return (manager.getLoggedUsers().containsKey(uuid));
  }
}
