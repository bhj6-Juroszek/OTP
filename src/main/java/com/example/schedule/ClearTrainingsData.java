package com.example.schedule;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.TrainingsDAO;

/**
 * Created by Bartek on 2017-05-08.
 */
public class ClearTrainingsData implements Runnable {
    @Override
    public void run() {
        DAOHandler.trainingsDAO.clear();
    }
}
