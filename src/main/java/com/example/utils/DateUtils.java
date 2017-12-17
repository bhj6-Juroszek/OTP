package com.example.utils;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public class DateUtils {

  public static Date addHoursToDate(@Nonnull final Date basicDate, final double hours) {
    final int addMinutes = (int)hours*60%60;
    final int addHours = (int)(hours*60-addMinutes)/60;
    final Calendar cal = getInstance(); // creates calendar
    cal.setTime(basicDate); // sets calendar time/date
    cal.add(HOUR_OF_DAY, addHours); // adds one hour
    cal.add(MINUTE, addMinutes);
    return cal.getTime();
  }

  public static  java.sql.Date getSQLDate(@Nonnull final Date date) {
    return new java.sql.Date(date.getTime());
  }

  public Date prepareDate(final int year, final int month, final int date,
      final int hour, final int minute) {
    return new Date(LocalDateTime.of(year, month, date, hour, minute).toEpochSecond(ZoneOffset.UTC) * 1000);
  }
}
