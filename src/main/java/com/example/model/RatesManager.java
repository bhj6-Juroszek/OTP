package com.example.model;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.RatesDAO;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.entities.Rate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bartek on 2017-03-11.
 */
public class RatesManager {


    RatesDAO ratesDAO = DAOHandler.ratesDAO;


    public boolean rateProfile(User fromUser, Profile ratedProfile, int value, String comment)
    {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        Rate result=new Rate(comment, value, ratedProfile.getUserId(), fromUser.getId(),date);
        return ratesDAO.saveToDB(result);
    }

    public double getProfileAverageRate(Profile profile)
    {
        ArrayList<Rate> rates= ratesDAO.getRatesByProfile(profile.getId());
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
        ArrayList<Rate> rates= ratesDAO.getRatesByProfile(profile.getId());
        int max=5;
        if((rates.size()+1<max))max=rates.size()-1;
        return new ArrayList(rates.subList(0,max));
    }

    public ArrayList<Rate> getProfileRates(Profile profile)
    {
        return ratesDAO.getRatesByProfile(profile.getId());

    }

    public Rate ifUserRated(User user, Profile profile)
    {
        return ratesDAO.getProfileUserRate(profile.getId(), user.getId());

    }







}
