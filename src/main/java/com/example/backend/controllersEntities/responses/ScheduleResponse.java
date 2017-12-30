package com.example.backend.controllersEntities.responses;

import com.example.backend.model.Week;
import com.example.daoLayer.entities.Training;

import java.util.ArrayList;
import java.util.List;

public class ScheduleResponse extends Response {
  private Week scheduleWeek;
  private List<Training> trainings;

  public ScheduleResponse() {
    this.trainings = new ArrayList<>();
    this.scheduleWeek = new Week();
  }

  public Week getScheduleWeek() {
    return scheduleWeek;
  }

  public void setScheduleWeek(final Week scheduleWeek) {
    this.scheduleWeek = scheduleWeek;
  }

  public List<Training> getTrainings() {
    return trainings;
  }

  public void setTrainings(final List<Training> trainings) {
    this.trainings = trainings;
  }
}
