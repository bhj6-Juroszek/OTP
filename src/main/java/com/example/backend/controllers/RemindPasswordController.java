package com.example.backend.controllers;

import com.example.backend.helpers.PasswordReminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@Controller
@RequestMapping("/")
public class RemindPasswordController {

  private final PasswordReminder passwordReminder;

  @RequestMapping(value = "/resetPassword", method = GET)
  public ResponseEntity<String> remindPassword(@RequestParam("mail") final String mail) {
    if (passwordReminder.changePassword(mail)) {
      return new ResponseEntity<>(OK);
    }
    return new ResponseEntity<>(UNAUTHORIZED);
  }

  @Autowired
  public RemindPasswordController(@Nonnull final PasswordReminder passwordReminder) {
    this.passwordReminder = passwordReminder;
  }
}
