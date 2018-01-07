package com.example.backend.model;

public class Day {
  private String name;
  private int day;
  private int month;

  public int getDay() {
    return day;
  }

  public void setDay(final int day) {
    this.day = day;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(final int month) {
    this.month = month;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
