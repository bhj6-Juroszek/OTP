package com.example.backend.model;

import com.example.backend.contexts.UserContext;
import com.example.backend.controllersEntities.responses.LoginResponse;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.User;
import com.example.utils.MailManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static com.example.utils.ResponseCode.*;
import static com.example.utils.SessionManager.HOST_NAME;

/**
 * Created by Bartek on 2017-04-29.
 */
@Service
public class UserManager {

  private static final Logger LOGGER = LogManager.getLogger(UserManager.class);
  private final static String FROM = "Online training Platform";
  private final static String SUBJECT = "Yor account registration";
  private final static String CONTENT_TEMPLATE = "Your account has been created. Click http://%sconfirm.html?id=%s to" +
      " finish registration.";

  private final UsersDAO usersDAO;
  private final MailManager mailManager;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserManager(@Nonnull final UsersDAO usersDAO, @Nonnull final MailManager mailManager) {
    this.usersDAO = usersDAO;
    this.mailManager = mailManager;
    this.passwordEncoder = new BCryptPasswordEncoder(12);
  }

  public LoginResponse login(@Nonnull final String login, @Nonnull final String password) {

    final LoginResponse response = new LoginResponse();
    final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    final User user = usersDAO.getUserByLogin(login);
    if (user == null) {
      response.setResponseCode(ACCOUNT_DOESNT_EXISTS);
    } else if (!passwordEncoder.matches(password, user.getPassword())) {
      response.setResponseCode(WRONG_PASSWORD);
    } else if (!user.getConfirmation().equals("")) {
      response.setResponseCode(NOT_AUTHENTICATED);
    } else {
      final UserContext userContext = new UserContext();
      userContext.setUser(user);
      response.setUserContext(userContext);
      response.setResponseCode(SUCCESS);
    }
    LOGGER.info("Login request for mail={}, password={} ended with responseCode={}", login, password,
        response.getResponseCode());
    return response;
  }

  public void confirmAccount(@Nonnull final String confirmationUUID) {
    final User user = usersDAO.getUserByConfirmation(confirmationUUID);
    if (user != null) {
      user.setConfirmation("");
      usersDAO.updateRecord(user);
      LOGGER.info("Confirmed mail from {}", user);
    }
  }

  public int register(@Nonnull final String mail, @Nonnull final String password) {
    if (usersDAO.existsAnother(mail, "mail", "")) {
      return ACCOUNT_EXISTS;
    }
    final String token = UUID.randomUUID().toString();
    final boolean mailSuccess = mailManager.sendMail(FROM, mail, SUBJECT, getContent(token));
    if (mailSuccess) {
      final String hashedPassword = passwordEncoder.encode(password);
      final User registeredUser = new User("Unknown",
          "Unknown",
          mail,
          mail,
          hashedPassword,
          false,
          "",
          token);
      usersDAO.saveToDB(registeredUser);
      LOGGER.info("Registration of {}", registeredUser);
      return SUCCESS;
    }
    return INVALID_DATA;
  }

  private String getContent(@Nonnull final String uuid) {
    return String.format(CONTENT_TEMPLATE, HOST_NAME, uuid);
  }

  public static Boolean testImage(@Nonnull final String url) {
    if (url.equals("")) {
      return false;
    }
    try {
      final BufferedImage image = ImageIO.read(new URL(url));
      if (image != null) {
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      return false;
    }
  }
}
