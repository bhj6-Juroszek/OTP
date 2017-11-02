package com.example.model;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.UsersDAO;
import com.example.backend.utils.MailManager;
import com.example.daoLayer.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

/**
 * Created by Bartek on 2017-03-09.
 */
public class PasswordReminder {

    private MailManager sender=new MailManager();
    private UsersDAO customersRep= DAOHandler.usersDAO;
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
            User user =customersRep.getCustomerByMail(mail);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            customersRep.updateRecord(user);

            sender.sendMail("Juroszek",
                     mail,
                    "Your password has been changed",
                    "Your new Password: "+newPassword);
            return true;
        }
        return false;
    }
}
