package com.example.backend.controllers;

import com.example.backend.services.PasswordReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@Controller
@RequestMapping("/")
public class RemindPasswordController extends AuthenticatedController {

  private final PasswordReminderService passwordReminderService;

  @RequestMapping(value = "/password", method = POST)
  public ResponseEntity remindPassword(@RequestParam("mail") final String mail) {
    return passwordReminderService.changePassword(mail).map(user -> {
      manager.removeUser(user);
      return new ResponseEntity<>(OK);
    })
        .orElse(new ResponseEntity<>(UNAUTHORIZED));
  }

  @Autowired
  public RemindPasswordController(@Nonnull final PasswordReminderService passwordReminderService) {
    this.passwordReminderService = passwordReminderService;
  }
}
