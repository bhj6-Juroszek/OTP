package com.example.schedule;

import com.example.daoLayer.daos.CustomersDAO;
import com.example.daoLayer.daos.SimpleMailManager;
import com.example.daoLayer.daos.TrainingsDAO;
import com.example.daoLayer.entities.Customer;
import com.example.daoLayer.entities.Training;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bartek on 2017-05-08.
 */
public class Reminder implements Runnable {
    private SimpleMailManager mailManager=new SimpleMailManager();
    @Override
    public void run() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        java.sql.Date sqlDate = new java.sql.Date(c.getTime().getTime());
        ArrayList<Training> trainings=TrainingsDAO.getInstance().getTrainingsFromDate(sqlDate);
        for(Training t:trainings)
        {
            if(t.getTakenById()!=-1)
            {
                Customer reservedBy=(CustomersDAO.getInstance().getCustomerById(t.getTakenById()));
                double hours=t.getHour()%1;
                double minutes=(t.getHour()-hours)*60;
                mailManager.sendMail("OTP",t.getOwner().getMail(),
                        String.format("Training on %s %f.0-%f.0 %s  is tomorrow!",t.getDate().toString(),hours,minutes,t.getCity()),
                        String.format("User data: %s %s . Contact: %s",reservedBy.getName(),reservedBy.getSurname(),reservedBy.getMail()));
                mailManager.sendMail("OTP",reservedBy.getMail(),
                        String.format("Training on %s %f.0-%f.0 %s  is tomorrow!",t.getDate().toString(),hours,minutes,t.getCity()),
                        String.format("Trainer data: %s %s . Contact: %s",t.getOwner().getName(),t.getOwner().getSurname(),t.getOwner().getMail()));
            }
        }

    }
}
