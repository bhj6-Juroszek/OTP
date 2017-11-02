package com.example.backend.controllers;

import com.example.backend.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public abstract class AuthenticatedController {
  @Autowired
  protected SessionManager manager;
}
