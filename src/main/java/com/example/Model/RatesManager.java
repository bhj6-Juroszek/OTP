package com.example.Model;

import com.example.DAOS.CustomersDAO;
import com.example.DAOS.ProfilesDAO;
import com.example.DAOS.RatesDAO;
import com.example.entities.Customer;
import com.example.entities.Profile;
import com.example.entities.Rate;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bartek on 2017-03-11.
 */
public class RatesManager {


    RatesDAO ratesRep=RatesDAO.getInstance();


    public boolean rateProfile(Customer fromCustomer, Profile ratedProfile, int value, String comment)
    {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        Rate result=new Rate(comment, value, ratedProfile.getId(), fromCustomer.getId(),date);
        return ratesRep.saveToDB(result);
    }

    public double getProfileAverageRate(Profile profile)
    {
        ArrayList<Rate> rates=ratesRep.getRatesByProfile(profile.getId());
        double result=0;
        double divader=rates.size();
        for(Rate r:rates)
        {
            result+=r.getValue();
        }
        result/=divader;
        return result;
    }

    public ArrayList<Rate> getProfileLastRates(Profile profile)
    {
        ArrayList<Rate> rates=ratesRep.getRatesByProfile(profile.getId());
        int max=5;
        if((rates.size()+1<max))max=rates.size()-1;
        return new ArrayList(rates.subList(0,max));
    }

    public ArrayList<Rate> getProfileRates(Profile profile)
    {
        return ratesRep.getRatesByProfile(profile.getId());

    }

    public Rate ifUserRated(Customer customer, Profile profile)
    {
        return ratesRep.getProfileUserRate(profile.getId(),customer.getId());

    }







}
