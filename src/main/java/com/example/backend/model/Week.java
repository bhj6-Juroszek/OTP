package com.example.backend.model;

import java.util.ArrayList;
import java.util.List;

public class Week {
  private List<Day> days;

  public Week() {
    this.days = new ArrayList<>();
  }

  public List<Day> getDays() {
    return days;
  }

  public void setDays(final List<Day> days) {
    this.days = days;
  }
}
