package com.example.backend.schedule;

import com.example.utils.MailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * Created by Bartek on 2017-05-08.
 */
@Component
public class Reminder implements Runnable {

  private final MailManager mailManager;

  @Autowired
  public Reminder(@Nonnull final MailManager mailManager) {
    this.mailManager = mailManager;
  }

  @Override
  public void run() {
//    final Calendar calendar = Calendar.getTrainingWithInstance();
//    calendar.setTime(new Date());
//    calendar.add(Calendar.DATE, 1);
//    java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());
//    ArrayList<Training> trainings = DAOHandler.trainingsDAO.getTrainingsFromDate(sqlDate);
//    for (Training t : trainings) {
//      if (t.getTakenById() != -1) {
//        final User reservedBy = (DAOHandler.usersDAO.getUserById(t.getTakenById()));
//        double hours = t.getDate().getHours();
//        double minutes = t.getDate().getMinutes();
//        mailManager.sendMail("OTP", t.getOwner().getMail(),
//            String.format("Training on %s %f.0-%f.0 %s  is tomorrow!", t.getDate().toString(), hours, minutes,
//                t.getCity()),
//            String.format("User data: %s. Contact: %s", reservedBy.getName(), reservedBy.getMail()));
//        mailManager.sendMail("OTP", reservedBy.getMail(),
//            String.format("Training on %s %f.0-%f.0 %s  is tomorrow!", t.getDate().toString(), hours, minutes,
//                t.getCity()),
//            String.format("Trainer data: %s. Contact: %s", t.getOwner().getName(), t.getOwner().getMail()));
//      }
//    }
//
//  }
  }
}
