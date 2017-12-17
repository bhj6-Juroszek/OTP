package com.example.backend.controllers;

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

  private UsersDAO usersDAO;
  private static final String REDIRECT = String.format("://%sindex.html", DOMAIN_NAME);

  @RequestMapping(value = "/confirm", method = RequestMethod.GET)
  public String processForm(@RequestParam("id") String token, HttpServletRequest request
  ) {
    if (token.equals("")) {
      return request.getScheme() + REDIRECT;

    }
    System.out.println(token);
    String redirectUrl = request.getScheme() + REDIRECT;
    final User cust = usersDAO.getUserByConfirmation(token);
    if (cust != null) {
      cust.setConfirmation("");
      usersDAO.updateRecord(cust);
    }
    return "redirect:" + redirectUrl;
  }

  @Autowired
  public void setUsersDAO(@Nonnull final UsersDAO usersDAO) {
    this.usersDAO = usersDAO;
  }
}
