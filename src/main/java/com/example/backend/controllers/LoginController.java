package com.example.backend.controllers;

import com.example.AccountsApplication;
import com.example.backend.controllersEntities.requests.LoginRequest;
import com.example.backend.controllersEntities.responses.LoginResponse;
import com.example.backend.utils.SessionManager;
import com.example.daoLayer.entities.Customer;
import com.example.model.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.backend.controllers.ResponseCode.*;

@CrossOrigin
@Controller
@RequestMapping("/")
public class LoginController {

  @Autowired
  private SessionManager manager;
  @Autowired
  UserManager userManager;


  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public @ResponseBody
  LoginResponse loginAction (@RequestBody LoginRequest request) {
    Customer cust = userManager.login(request.getEmail(), request.getPassword());
    final LoginResponse response = new LoginResponse();
    response.setResponseCode(INVALID);
    if(cust == null) return response;
    response.setResponseCode(SUCCESS);
    return null;
  }

  @RequestMapping(value = "/logins", method = RequestMethod.GET)
  public @ResponseBody
  void loginAction() {
    manager.addToMap("coś");
  }
}
