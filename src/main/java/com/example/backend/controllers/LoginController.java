package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.LoginRequest;
import com.example.backend.controllersEntities.responses.LoginResponse;
import com.example.backend.utils.ResponseCode;
import com.example.model.UserManager;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;

import static com.example.backend.utils.ResponseCode.ALREADY_LOGGED_IN;
import static java.util.UUID.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping("/")
public class LoginController extends AuthenticatedController {


  private final UserManager userManager;

  @RequestMapping(value = "/login", method = POST, consumes = "application/json")
  public @ResponseBody
  LoginResponse loginAction(@RequestBody LoginRequest request) {
    final LoginResponse response = userManager.login(request.getEmail(), request.getPassword());
    if(response.getResponseCode() == ResponseCode.SUCCESS) {
     if(!manager.addToMap(randomUUID().toString(), response.getUserContext().getUser())) {
       response.setResponseCode(ALREADY_LOGGED_IN);
     }
    }
    return response;
  }

  @Autowired
  public LoginController(@Nonnull final UserManager userManager) {
    this.userManager = userManager;
  }
}
