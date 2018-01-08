package com.example.utils;

import com.example.backend.utils.model.Day;
import com.example.backend.utils.model.Week;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIN;
import static java.util.Calendar.*;

public class DateUtils {

  private static final String[] DAYS = {
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday",
      "Sunday"};

  public static String getDay(final int day) {
    return DAYS[day];
  }

  public static Date addHoursToDate(@Nonnull final Date basicDate, final double hours) {
    final int addMinutes = (int)hours*60%60;
    final int addHours = (int)(hours*60-addMinutes)/60;
    final Calendar calendar = getInstance(); // creates calendar
    calendar.setTime(basicDate); // sets calendar time/date
    calendar.add(HOUR_OF_DAY, addHours); // adds one hour
    calendar.add(MINUTE, addMinutes);
    return calendar.getTime();
  }

  public static  java.sql.Date getSQLDate(@Nonnull final Date date) {
    return new java.sql.Date(date.getTime());
  }

  public Date prepareDate(final int year, final int month, final int date,
      final int hour, final int minute) {
    return new Date(LocalDateTime.of(year, month, date, hour, minute).toEpochSecond(ZoneOffset.UTC) * 1000);
  }
  public static Date getEndOfDay(@Nonnull final Date date) {
    final LocalDateTime localDateTime = dateToLocalDateTime(date);
    final LocalDateTime endOfDay = localDateTime.with(MAX);
    return localDateTimeToDate(endOfDay);
  }

  public static Date getStartOfDay(@Nonnull final Date date) {
    final LocalDateTime localDateTime = dateToLocalDateTime(date);
    final LocalDateTime startOfDay = localDateTime.with(MIN);
    return localDateTimeToDate(startOfDay);
  }

  private static Date localDateTimeToDate(@Nonnull final LocalDateTime startOfDay) {
    return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
  }

  private static LocalDateTime dateToLocalDateTime(@Nonnull final Date date) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
  }

  public static Date[] getWeekBoundariesFromDate(@Nonnull final Date date) {
    final Date[] result = new Date[2];
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    while( dayOfWeek != 2) {
      calendar.add(Calendar.DATE, -1);
      dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }
    result[0] = getStartOfDay(calendar.getTime());
    calendar.add(Calendar.DATE, 6);
    result[1] = getEndOfDay(calendar.getTime());
    return result;
  }

  public static Week getWeekFromDate(@Nonnull final Date date) {
    final Week week = new Week();
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    while( dayOfWeek != 2) {
      calendar.add(Calendar.DATE, -1);
      dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }
    for(int i=0; i<7; i++) {
      final Day day = new Day();
      day.setDay(dateToLocalDateTime(calendar.getTime()).getDayOfMonth());
      day.setMonth(dateToLocalDateTime(calendar.getTime()).getMonth().getValue());
      day.setName(getDay(i));
      week.getDays().add(day);
      calendar.add(Calendar.DATE, 1);
    }
    return week;
  }


}
