package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.RegisterRequest;
import com.example.backend.utils.SessionManager;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.DAOHandler;
import com.example.backend.utils.MailManager;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping("/")
public class RegisterController {


  @Autowired
  private SessionManager manager;

  @RequestMapping(value = "/register", method = POST, consumes = "application/json")
  public @ResponseBody
  int register(@RequestBody RegisterRequest request) {
    final UsersDAO usersDAO = DAOHandler.usersDAO;
    final String mail = request.getEmail();
    final String password = request.getPassword();
    final MailManager sender = new MailManager();
    System.out.print(request.getEmail());
    if (usersDAO.existsAnother(mail, "mail", (long) -1)) {
      return -1;
    }

    final String token = UUID.randomUUID().toString();
    if (sender.sendMail("Juroszek",
        mail,
        "Automatically generated Message",
        "Your account has been created. Click https://software-architecture.herokuapp.com/confirm.html?id=" + token +
            " to finish registration;")) {
      final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      final String hashedPassword = passwordEncoder.encode(password);

      usersDAO.saveToDB(new User("Unknown",
          "Unknown",
          mail,
          mail,
          hashedPassword,
          false,
          "",
          token));
      return 1;
    } else {
      return -2;
    }
  }
}
