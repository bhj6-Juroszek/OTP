package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.RegisterRequest;
import com.example.backend.helpers.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping("/")
public class RegisterController {

  private UserManager userManager;

  @RequestMapping(value = "/register", method = POST, consumes = "application/json")
  public @ResponseBody
  int register(@RequestBody RegisterRequest request) {
    final String mail = request.getEmail();
    final String password = request.getPassword();
    return userManager.register(mail, password);
  }

  @Autowired
  public void setUserManager(final UserManager userManager) {
    this.userManager = userManager;
  }

}
