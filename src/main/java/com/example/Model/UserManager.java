package com.example.Model;

import com.example.DAOS.CustomersDAO;
import com.example.DAOS.TrainingsDAO;
import com.example.entities.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bartek on 2017-04-29.
 */
public class UserManager {
    private CustomersDAO customerRep =CustomersDAO.getInstance();

    public Customer login(String login, String password) {
        Customer result = null;
        if (!login.equals("") && !password.equals("")) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            result = customerRep.getCustomerByLogin(login);
            if (result != null && passwordEncoder.matches(password, result.getPassword())) {
                return result;
            }

        }
        return null;
    }

    public static Boolean testImage(String url) {

        if(url==null || url.equals(""))
        {
            return false;
        }
        try {
            BufferedImage image = ImageIO.read(new URL(url));
            //BufferedImage image = ImageIO.read(new URL("http://someimage.jpg"));
            if (image != null) {
                return true;
            } else {
                return false;
            }
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
