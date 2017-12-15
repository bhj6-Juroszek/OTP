package com.example.backend.controllers;

import com.example.daoLayer.DAOHelper;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bartek on 2017-05-04.
 */
@CrossOrigin
@Controller
@RequestMapping("/")
public class RedirectController {

  private UsersDAO usersDAO;

  @RequestMapping(value = "/confirm", method = RequestMethod.GET)
  public String processForm(@RequestParam("id") String token, HttpServletRequest request
  ) {
    if (token.equals("")) {
      return request.getScheme() + "://67.209.115.104:8181/#!mainView";

    }
    System.out.println(token);
    String redirectUrl = "";
    final User cust = usersDAO.getCustomerByConfirmation(token);
    if (cust != null) {
      cust.setConfirmation("");
      usersDAO.updateRecord(cust);
      redirectUrl = request.getScheme() + "://67.209.115.104:8181/#!mainView";
      System.out.println(redirectUrl);
    } else {
      redirectUrl = request.getScheme() + "://67.209.115.104:8181/#!mainView";
      System.out.println(redirectUrl);
    }
    return "redirect:" + redirectUrl;
  }

  @Autowired
  public void setUsersDAO(final UsersDAO usersDAO) {
    this.usersDAO = usersDAO;
  }
}
