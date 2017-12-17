package com.example.backend.schedule;

import com.example.daoLayer.daos.TrainingsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * Created by Bartek on 2017-05-08.
 */
@Component
public class ClearTrainingsData implements Runnable {

  private final TrainingsDAO trainingsDAO;

  @Autowired
  public ClearTrainingsData(@Nonnull final TrainingsDAO trainingsDAO) {
    this.trainingsDAO = trainingsDAO;
  }

  @Override
  public void run() {
        trainingsDAO.clear(5);
  }
}
