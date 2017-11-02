package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.LoginRequest;
import com.example.backend.controllersEntities.responses.LoginResponse;
import com.example.model.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static java.util.UUID.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping("/")
public class LoginController extends AuthenticatedController {

  @Autowired
  UserManager userManager;

  @RequestMapping(value = "/login", method = POST, consumes = "application/json")
  public @ResponseBody
  LoginResponse loginAction(@RequestBody LoginRequest request) {
    final LoginResponse response = userManager.login(request.getEmail(), request.getPassword());
    manager.addToMap(randomUUID().toString(), response.getUserContext().getUser());
    return response;
  }

}
