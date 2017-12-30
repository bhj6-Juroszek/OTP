package com.example.backend.helpers;

import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.User;
import com.example.utils.MailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Created by Bartek on 2017-03-09.
 */
@Service
public class PasswordReminder {

  private static final int NUMBER_OF_LETTERS = 15;
  private final MailManager sender;
  private final UsersDAO customersRep;

  @Autowired
  public PasswordReminder(@Nonnull final MailManager sender, @Nonnull final UsersDAO customersRep) {
    this.sender = sender;
    this.customersRep = customersRep;
  }

  private static String generatePassword() {
    final StringBuilder result = new StringBuilder();
    final Random random = new Random();
    for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
      char letter = (char) (random.nextInt(61) + 48);
      if (letter > 57 && letter < 83) {
        letter = (char) (letter + 7);
      } else if (letter > 82) {
        letter = (char) (letter + 14);
      }
      result.append(letter);
    }
    return result.toString();
  }

  public boolean changePassword(@Nonnull final String mail) {
    final String newPassword = generatePassword();
    if (customersRep.existsAnother(mail, "mail", "")) {
      final User user = customersRep.getUserByMail(mail);
      final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      final String hashedPassword = passwordEncoder.encode(newPassword);
      user.setPassword(hashedPassword);
      customersRep.updatePassword(user.getId(), hashedPassword);
      sender.sendMail("OTP",
          mail,
          "Your password has been changed",
          "Your new Password: " + newPassword);
      return true;
    }
    return false;
  }
}
