package com.example.backend.model;

import com.example.daoLayer.daos.TrainingsDAO;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.Place;
import com.example.daoLayer.entities.Training;
import com.example.utils.MailManager;
import com.example.utils.ResponseCode;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Calendar;
import java.util.Date;

import static com.example.utils.ResponseCode.*;

/**
 * Created by Bartek on 2017-03-25.
 */
@Service
public class TrainingManager {

  private final MailManager mailManager;
  private final TrainingsDAO trainingsDAO;
  private JsonReader jsonReader;

  @Autowired
  public TrainingManager(@Nonnull final MailManager mailManager, @Nonnull final TrainingsDAO trainingsDAO, @Nonnull final JsonReader jsonReader) {
    this.mailManager = mailManager;
    this.trainingsDAO = trainingsDAO;
    this.jsonReader = jsonReader;
  }

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

  public static Date[] getWeekFromDate(@Nonnull final Date date) {
    final Date[] result = new Date[7];
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    dayOfWeek -= 1;
    if (dayOfWeek == 0) {
      dayOfWeek = 7;
    }
    calendar.add(Calendar.DATE, -(dayOfWeek - 1));
    for (int i = 0; i < 7; i++) {
      result[i] = calendar.getTime();
      calendar.add(Calendar.DATE, 1);
    }

    return result;
  }

  public int saveTrainingTemplate(@Nonnull final Training training, @Nonnull final String placeName, @Nonnull final String categoryId) {
    final Place place = jsonReader.getPlace(placeName);
    if(place != null) {
      final Category idPlaceholder = new Category();
      idPlaceholder.setId(categoryId);
      training.setPlace(place);
      training.setCategory(idPlaceholder);
      trainingsDAO.saveTraining(training);
      return SUCCESS;
    }
    return INVALID_DATA;
  }


//  public boolean saveTraining(@Nonnull final Training training,@Nonnull final  Date date) {
//    final java.sql.Date sqlDate = getSQLDate(date);
//    training.setDate(sqlDate);
//    return trainingsDAO.saveToDB(training);
//  }
//
//  public ArrayList<Training> getTrainingsFromDate(@Nonnull final Date date,@Nonnull final  Long fromId) {
//    final java.sql.Date sqlDate = getSQLDate(date);
//    return trainingsDAO.getTrainingsFromDate(fromId, sqlDate);
//  }
//
//  public void reserve(@Nonnull final Training training, final long fromId) {
//    training.setTakenById(fromId);
//    final User reservedBy = (DAOHandler.usersDAO.getUserById(training.getTakenById()));
//    trainingsDAO.updateRecord(training);
//    final double minutes = training.getDate().getMinutes();
//    final double hours = training.getDate().getHours();
//    mailManager.sendMail("OTP", training.getOwner().getMail(),
//        String.format("Training on %s %f.0-%f.0 %s  has been reserved!", training.getDate().toString(), hours, minutes,
//            training.getCity()),
//        String.format("User data: %s. Contact: %s", reservedBy.getName(), reservedBy.getMail()));
//    mailManager.sendMail("OTP", reservedBy.getMail(),
//        String.format("You have just reserved training on %s %f.0-%f.0 %s !", training.getDate().toString(), hours,
//            minutes, training.getCity()),
//        String.format("Trainer data: %s. Contact: %s", training.getOwner().getName(), training.getOwner().getMail()));
//  }
//
//  public void remove(@Nonnull final Training training) {
//    trainingsDAO.delete(training);
//  }
//
//  @Nullable
//  public List<Training> getTrainingsByFilters(final String cityName, final double range, final long categoryId, @Nonnull final Date dateFirst,
//      @Nonnull final Date dateLast, final double maxPrice, final String sortBy, final boolean showOnline) {
//    java.sql.Date sqlDateFirst = getSQLDate(dateFirst);
//    java.sql.Date sqlDateLast = getSQLDate(dateLast);
//    return trainingsDAO
//        .getTrainingsByFilter(cityName, range, categoryId, sqlDateFirst, sqlDateLast, maxPrice, sortBy, showOnline);
//  }


}
