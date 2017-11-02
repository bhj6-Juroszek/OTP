package com.example.schedule;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.UsersDAO;
import com.example.backend.utils.MailManager;
import com.example.daoLayer.daos.TrainingsDAO;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.entities.Training;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bartek on 2017-05-08.
 */
public class Reminder implements Runnable {

  private MailManager mailManager = new MailManager();

  @Override
  public void run() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DATE, 1);
    java.sql.Date sqlDate = new java.sql.Date(c.getTime().getTime());
    ArrayList<Training> trainings = DAOHandler.trainingsDAO.getTrainingsFromDate(sqlDate);
    for (Training t : trainings) {
      if (t.getTakenById() != -1) {
        final User reservedBy = (DAOHandler.usersDAO.getCustomerById(t.getTakenById()));
        double hours = t.getDate().getHours();
        double minutes = t.getDate().getMinutes();
        mailManager.sendMail("OTP", t.getOwner().getMail(),
            String.format("Training on %s %f.0-%f.0 %s  is tomorrow!", t.getDate().toString(), hours, minutes,
                t.getCity()),
            String.format("User data: %s. Contact: %s", reservedBy.getName(), reservedBy.getMail()));
        mailManager.sendMail("OTP", reservedBy.getMail(),
            String.format("Training on %s %f.0-%f.0 %s  is tomorrow!", t.getDate().toString(), hours, minutes,
                t.getCity()),
            String.format("Trainer data: %s. Contact: %s", t.getOwner().getName(), t.getOwner().getMail()));
      }
    }

  }
}
