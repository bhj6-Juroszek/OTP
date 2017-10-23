package com.example.model;

import com.example.daos.CustomersDAO;
import com.example.daos.SimpleMailManager;
import com.example.entities.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

/**
 * Created by Bartek on 2017-03-09.
 */
public class PasswordReminder {

    private SimpleMailManager sender=new SimpleMailManager();
    private CustomersDAO customersRep=CustomersDAO.getInstance();
    private static final int NUMBER_OF_LETTERS=15;
    private String mail;


    public PasswordReminder(String mail)
    {
        this.mail=mail;
    }

    public static String generatePassword()
    {
        StringBuilder result=new StringBuilder();
        Random random=new Random();
        for(int i=0; i<NUMBER_OF_LETTERS; i++) {
            char letter = (char) (random.nextInt(61) + 48);
            if(letter>57 && letter<83)
            {
                letter=(char)(letter+7);
            }
            else if(letter>82)
            {
                letter=(char)(letter+14);
            }
            result.append(letter);
        }
        return result.toString();
    }

    public boolean changePassword()
    {
        String newPassword=generatePassword();
        if(customersRep.existsAnother(mail,"mail",(long)-1))
        {
            Customer customer=customersRep.getCustomerByMail(mail);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(newPassword);
            customer.setPassword(hashedPassword);
            customersRep.updateRecord(customer);

            sender.sendMail("Juroszek",
                     mail,
                    "Your password has been changed",
                    "Your new Password: "+newPassword);
            return true;
        }
        return false;
    }
}
