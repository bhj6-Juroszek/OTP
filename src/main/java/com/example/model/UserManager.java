package com.example.model;

import com.example.backend.contexts.UserContext;
import com.example.backend.controllersEntities.responses.LoginResponse;
import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static com.example.backend.utils.ResponseCode.*;

/**
 * Created by Bartek on 2017-04-29.
 */
@Service
public class UserManager {

  private final UsersDAO usersDAO = DAOHandler.usersDAO;

  public LoginResponse login(@Nonnull final String login, @Nonnull final String password) {
    final LoginResponse response = new LoginResponse();
    final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    final User user = usersDAO.getCustomerByLogin(login);
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
    return response;
  }

  public static Boolean testImage(@Nonnull final String url) {
    if (url.equals("")) {
      return false;
    }
    try {
      BufferedImage image = ImageIO.read(new URL(url));
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
