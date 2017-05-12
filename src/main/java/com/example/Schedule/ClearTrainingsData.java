package com.example.Schedule;

import com.example.DAOS.TrainingsDAO;

/**
 * Created by Bartek on 2017-05-08.
 */
public class ClearTrainingsData implements Runnable {
    @Override
    public void run() {
        TrainingsDAO.getInstance().clear();
    }
}
