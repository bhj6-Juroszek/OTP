package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.RegisterRequest;
import com.example.backend.utils.SessionManager;
import com.example.daoLayer.daos.CustomersDAO;
import com.example.daoLayer.daos.DAOHandler;
import com.example.daoLayer.daos.SimpleMailManager;
import com.example.daoLayer.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@Controller
@RequestMapping("/")
public class RegisterController {


  @Autowired
  private SessionManager manager;

  @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
  public @ResponseBody
  int register(@RequestBody RegisterRequest request) {
    final CustomersDAO customersDao = DAOHandler.custDao;
    final String mail = request.getEmail();
    final String password = request.getPassword();
    final SimpleMailManager sender = new SimpleMailManager();
    System.out.print(request.getEmail());
    if (customersDao.existsAnother(mail, "mail", (long) -1)) {
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

      customersDao.saveToDB(new Customer("Unknown",
          "Unknown",
          "Unknown",
          mail,
          mail,
          hashedPassword,
          false,
          "",
          "",
          -1,
          token));
      return 1;
    } else {
      return -2;
    }
  }
}
