package com.example.gui.ui;

import com.example.DAOS.CustomersDAO;
import com.example.entities.Customer;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.Notification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bartek on 2017-05-04.
 */
@Controller
@RequestMapping("/")
public class RedirectController  {



    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String processForm(@RequestParam("id") String token, HttpServletRequest request
                              )
    {
        if(token.equals(""))
        {
            return request.getScheme()+"://software-architecture.herokuapp.com/#!mainView";

        }
        System.out.println(token);
        CustomersDAO dao=CustomersDAO.getInstance();
        String redirectUrl="";
        Customer cust=dao.getCustomerByConfirmation(token);
        if(cust!=null) {
            cust.setConfirmation("");
            dao.updateRecord(cust);
            redirectUrl=  request.getScheme()+"://software-architecture.herokuapp.com/#!mainView";
            System.out.println(redirectUrl);
        }
        else{
            redirectUrl=  request.getScheme()+"://software-architecture.herokuapp.com/#!mainView";
            System.out.println(redirectUrl);
        }
        return "redirect:" + redirectUrl;
    }
    }
