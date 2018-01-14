package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.RegisterRequest;
import com.example.backend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/")
public class RegisterController {

  private UsersService usersService;

  @RequestMapping(value = "/register", method = POST, consumes = "application/json")
  public @ResponseBody
  int register(@RequestBody RegisterRequest request) {

    return usersService.register(request.getEmail(), request.getPassword());
  }

  @Autowired
  public void setUsersService(final UsersService usersService) {
    this.usersService = usersService;
  }

}
