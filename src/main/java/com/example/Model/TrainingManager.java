package com.example.Model;

import com.example.DAOS.CustomersDAO;
import com.example.DAOS.SimpleMailManager;
import com.example.DAOS.TrainingsDAO;
import com.example.entities.Category;
import com.example.entities.Customer;
import com.example.entities.Training;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bartek on 2017-03-25.
 */
public class TrainingManager {

    private SimpleMailManager mailManager=new SimpleMailManager();
    private TrainingsDAO trainingsRep=TrainingsDAO.getInstance();

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
       return trainingsRep.saveToDB(training);
    }

    public ArrayList<Training> getTrainingsFromDate(Date date, Long fromId)
    {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return trainingsRep.getTrainingsFromDate(fromId,sqlDate);
    }

    public void reserve(Training training, long fromId)
    {
        training.setTakenById(fromId);
        Customer reservedBy=(CustomersDAO.getInstance().getCustomerById(training.getTakenById()));
        trainingsRep.updateRecord(training);
        double hours=training.getHour()%1;
        double minutes=(training.getHour()-hours)*60;
        mailManager.sendMail("OTP",training.getOwner().getMail(),
                String.format("Training on %s %f.0-%f.0 %s  has been reserved!",training.getDate().toString(),hours,minutes,training.getCity()),
                String.format("User data: %s %s . Contact: %s",reservedBy.getName(),reservedBy.getSurname(),reservedBy.getMail()));
        mailManager.sendMail("OTP",reservedBy.getMail(),
                String.format("You have just reserved training on %s %f.0-%f.0 %s !",training.getDate().toString(),hours,minutes,training.getCity()),
                String.format("Trainer data: %s %s . Contact: %s",training.getOwner().getName(),training.getOwner().getSurname(),training.getOwner().getMail()));
    }
    public void remove(Training training)
    {
        trainingsRep.delete(training);
    }


    public ArrayList<Training> getTrainingsByFilters(String cityName, double range, Category cat, Date dateFirst, Date dateLast, double maxPrice, String sortBy, boolean showOnline )
    {
        java.sql.Date sqlDateFirst = new java.sql.Date(dateFirst.getTime());
        java.sql.Date sqlDateLast = new java.sql.Date(dateLast.getTime());
        return trainingsRep.getTrainingsByFilter(cityName, range, cat,  sqlDateFirst, sqlDateLast, maxPrice,sortBy,showOnline);
    }



}
