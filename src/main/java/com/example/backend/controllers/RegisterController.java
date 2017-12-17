package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.RegisterRequest;
import com.example.backend.model.UserManager;
import com.example.utils.SessionManager;
import com.example.daoLayer.daos.UsersDAO;
import com.example.utils.MailManager;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.example.utils.ResponseCode.ACCOUNT_EXISTS;
import static com.example.utils.ResponseCode.INVALID_DATA;
import static com.example.utils.ResponseCode.SUCCESS;
import static com.example.utils.SessionManager.HOST_NAME;
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
