package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.LoginRequest;
import com.example.backend.controllersEntities.responses.LoginResponse;
import com.example.backend.services.UsersService;
import com.example.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nonnull;

import static com.example.utils.ResponseCode.ALREADY_LOGGED_IN;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/")
public class LoginController extends AuthenticatedController {


  private final UsersService usersService;

  @RequestMapping(value = "/login", method = POST, consumes = "application/json")
  public @ResponseBody
  LoginResponse loginAction(@RequestBody LoginRequest request) {
    final LoginResponse response = usersService.login(request.getEmail(), request.getPassword());
    if(response.getResponseCode() == ResponseCode.SUCCESS) {
     if(!manager.addToMap(response.getUuid(), response.getUserContext().getUser())) {
       response.setResponseCode(ALREADY_LOGGED_IN);
       response.setUserContext(null);
       response.setUuid(null);
     }
    }
    return response;
  }

  @Autowired
  public LoginController(@Nonnull final UsersService usersService) {
    this.usersService = usersService;
  }
}
