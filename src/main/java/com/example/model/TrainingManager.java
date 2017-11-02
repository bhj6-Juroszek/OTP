package com.example.model;

import com.example.daoLayer.DAOHandler;
import com.example.backend.utils.MailManager;
import com.example.daoLayer.daos.TrainingsDAO;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.entities.Training;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bartek on 2017-03-25.
 */
public class TrainingManager {

    private MailManager mailManager=new MailManager();
    private TrainingsDAO trainingsDAO =DAOHandler.trainingsDAO;

    public static final String[] DAYS={
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"};


    public static String getDay(int day)
    {
        return DAYS[day];

    }

    public static Date[] getWeekFromDate(Date date)
    {
        Date[] result=new Date[7];
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayOfWeek-=1;
        if(dayOfWeek==0)dayOfWeek=7;
        c.add(Calendar.DATE, -(dayOfWeek-1));
        for(int i=0;i<7;i++)
        {
            result[i]=c.getTime();
            c.add(Calendar.DATE, 1);
        }

    return result;
    }


    public boolean saveTraining(Training training, Date date)
    {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        training.setDate(sqlDate);
       return trainingsDAO.saveToDB(training);
    }

    public ArrayList<Training> getTrainingsFromDate(Date date, Long fromId)
    {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return trainingsDAO.getTrainingsFromDate(fromId,sqlDate);
    }

    public void reserve(Training training, long fromId)
    {
        training.setTakenById(fromId);
        User reservedBy=(DAOHandler.usersDAO.getCustomerById(training.getTakenById()));
        trainingsDAO.updateRecord(training);
        double minutes=training.getDate().getMinutes();
        double hours=training.getDate().getHours();
        mailManager.sendMail("OTP",training.getOwner().getMail(),
                String.format("Training on %s %f.0-%f.0 %s  has been reserved!",training.getDate().toString(),hours,minutes,training.getCity()),
                String.format("User data: %s. Contact: %s",reservedBy.getName(),reservedBy.getMail()));
        mailManager.sendMail("OTP",reservedBy.getMail(),
                String.format("You have just reserved training on %s %f.0-%f.0 %s !",training.getDate().toString(),hours,minutes,training.getCity()),
                String.format("Trainer data: %s. Contact: %s",training.getOwner().getName(),training.getOwner().getMail()));
    }
    public void remove(Training training)
    {
        trainingsDAO.delete(training);
    }


    public ArrayList<Training> getTrainingsByFilters(String cityName, double range, Category cat, Date dateFirst, Date dateLast, double maxPrice, String sortBy, boolean showOnline )
    {
        java.sql.Date sqlDateFirst = new java.sql.Date(dateFirst.getTime());
        java.sql.Date sqlDateLast = new java.sql.Date(dateLast.getTime());
        return trainingsDAO
            .getTrainingsByFilter(cityName, range, cat,  sqlDateFirst, sqlDateLast, maxPrice,sortBy,showOnline);
    }



}
