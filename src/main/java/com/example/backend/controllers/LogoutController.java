package com.example.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/")
public class LogoutController extends AuthenticatedController {

  @RequestMapping(value = "/logout", method = POST)
  public ResponseEntity<String> logoutAction(@RequestParam("uuid") final String uuid) {
    if (super.authenticate(uuid)) {
      manager.getLoggedUsers().remove(uuid);
      return new ResponseEntity<>(OK);
    }
    return new ResponseEntity<>(UNAUTHORIZED);
  }

}
