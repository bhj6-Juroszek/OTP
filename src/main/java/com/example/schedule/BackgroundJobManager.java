package com.example.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bartek on 2017-05-08.
 */
@Component
@WebListener
public class BackgroundJobManager implements ServletContextListener {

  private final ClearTrainingsData clearTrainingsData;
  private final Reminder reminder;

  @Autowired
  public BackgroundJobManager(@Nonnull final ClearTrainingsData clearTrainingsData, @Nonnull final Reminder reminder) {
    this.clearTrainingsData = clearTrainingsData;
    this.reminder = reminder;
  }

  private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(clearTrainingsData, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(reminder, 0, 1, TimeUnit.HOURS);

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

}