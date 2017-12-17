package com.example.backend.controllers;

import com.example.backend.model.UserManager;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

import static com.example.utils.SessionManager.DOMAIN_NAME;

/**
 * Created by Bartek on 2017-05-04.
 */
@CrossOrigin
@Controller
@RequestMapping("/")
public class RedirectController {

  private UserManager userManager;
  private static final String REDIRECT = String.format("://%sindex.html", DOMAIN_NAME);

  @RequestMapping(value = "/confirm", method = RequestMethod.GET)
  public String processForm(@RequestParam("id") String token, HttpServletRequest request) {
    final String redirectUrl ="redirect:"+ request.getScheme() + REDIRECT;
    if (!token.equals("")) {
      userManager.confirmAccount(token);
    }
    return redirectUrl;
  }

  @Autowired
  public void setUserManager(@Nonnull final UserManager userManager) {
    this.userManager = userManager;
  }
}
