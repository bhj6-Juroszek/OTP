package com.example.backend.controllers;

import com.example.backend.controllersEntities.requests.RegisterRequest;
import com.example.backend.utils.SessionManager;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.DAOHelper;
import com.example.backend.utils.MailManager;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.example.backend.utils.ResponseCode.ACCOUNT_EXISTS;
import static com.example.backend.utils.ResponseCode.INVALID_DATA;
import static com.example.backend.utils.ResponseCode.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping("/")
public class RegisterController {

  private SessionManager manager;
  private UsersDAO usersDAO;
  private MailManager sender;

  @RequestMapping(value = "/register", method = POST, consumes = "application/json")
  public @ResponseBody
  int register(@RequestBody RegisterRequest request) {
    final String mail = request.getEmail();
    final String password = request.getPassword();
    System.out.print(request.getEmail());
    if (usersDAO.existsAnother(mail, "mail", (long) -1)) {
      return ACCOUNT_EXISTS;
    }

    final String token = UUID.randomUUID().toString();
    if (sender.sendMail("Juroszek",
        mail,
        "Automatically generated Message",
        "Your account has been created. Click https://software-architecture.herokuapp.com/confirm.html?id=" + token +
            " to finish registration;")) {
      final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
      final String hashedPassword = passwordEncoder.encode(password);

      usersDAO.saveToDB(new User("Unknown",
          "Unknown",
          mail,
          mail,
          hashedPassword,
          false,
          "",
          token));
      return SUCCESS;
    } else {
      return INVALID_DATA;
    }
  }

  @Autowired
  public void setManager(@Nonnull final SessionManager manager) {
    this.manager = manager;
  }

  @Autowired
  public void setUsersDAO(final UsersDAO usersDAO) {
    this.usersDAO = usersDAO;
  }

  @Autowired
  public void setSender(@Nonnull final MailManager sender) {
    this.sender = sender;
  }
}
