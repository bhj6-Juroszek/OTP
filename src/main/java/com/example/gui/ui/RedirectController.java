package com.example.gui.ui;

import com.example.daos.CustomersDAO;
import com.example.entities.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bartek on 2017-05-04.
 */
@Controller
@RequestMapping("/")
public class RedirectController {

  @RequestMapping(value = "/confirm", method = RequestMethod.GET)
  public String processForm(@RequestParam("id") String token, HttpServletRequest request
  ) {
    if (token.equals("")) {
      return request.getScheme() + "://software-architecture.herokuapp.com/#!mainView";

    }
    System.out.println(token);
    CustomersDAO dao = CustomersDAO.getInstance();
    String redirectUrl = "";
    Customer cust = dao.getCustomerByConfirmation(token);
    if (cust != null) {
      cust.setConfirmation("");
      dao.updateRecord(cust);
      redirectUrl = request.getScheme() + "://software-architecture.herokuapp.com/#!mainView";
      System.out.println(redirectUrl);
    } else {
      redirectUrl = request.getScheme() + "://software-architecture.herokuapp.com/#!mainView";
      System.out.println(redirectUrl);
    }
    return "redirect:" + redirectUrl;
  }
}
