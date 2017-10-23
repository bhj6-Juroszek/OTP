package com.example.schedule;

import com.example.daos.TrainingsDAO;

/**
 * Created by Bartek on 2017-05-08.
 */
public class ClearTrainingsData implements Runnable {
    @Override
    public void run() {
        TrainingsDAO.getInstance().clear();
    }
}
