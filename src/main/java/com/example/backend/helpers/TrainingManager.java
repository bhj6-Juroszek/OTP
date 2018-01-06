package com.example.backend.helpers;

import com.example.backend.controllersEntities.requests.BookingRequest;
import com.example.backend.controllersEntities.responses.BookingResponse;
import com.example.backend.controllersEntities.responses.ScheduleResponse;
import com.example.daoLayer.daos.TrainingsDAO;
import com.example.daoLayer.entities.*;
import com.example.utils.DateUtils;
import com.example.utils.MailManager;
import com.example.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

import static com.example.utils.DateUtils.*;
import static com.example.utils.ResponseCode.GENERAL_FAIL;
import static com.example.utils.ResponseCode.INVALID_DATA;
import static com.example.utils.ResponseCode.SUCCESS;

/**
 * Created by Bartek on 2017-03-25.
 */
@Service
public class TrainingManager {

  private final MailManager mailManager;
  private final TrainingsDAO trainingsDAO;
  private final JsonReader jsonReader;
  private final SessionManager sessionManager;

  @Autowired
  public TrainingManager(@Nonnull final MailManager mailManager, @Nonnull final TrainingsDAO trainingsDAO,
      @Nonnull final JsonReader jsonReader, @Nonnull final SessionManager sessionManager) {
    this.mailManager = mailManager;
    this.trainingsDAO = trainingsDAO;
    this.jsonReader = jsonReader;
    this.sessionManager = sessionManager;
  }

  public ScheduleResponse resolveScheduleResponse(@Nonnull final ScheduleResponse scheduleResponse,
      @Nonnull final String trainerId, @Nonnull final Date date) {
    scheduleResponse.setTrainings(getTrainerSchedule(trainerId, date));
    scheduleResponse.setScheduleWeek(DateUtils.getWeekFromDate(date));
    return scheduleResponse;
  }

  public int saveTrainingTemplate(@Nonnull final Training training, @Nonnull final String placeName,
      @Nonnull final String categoryId) {
    final Place place = jsonReader.getPlace(placeName);
    if (place != null) {
      final Category idPlaceholder = new Category();
      idPlaceholder.setId(categoryId);
      training.setPlace(place);
      training.setCategory(idPlaceholder);
      trainingsDAO.saveTraining(training);
      return SUCCESS;
    }
    return INVALID_DATA;
  }

  public synchronized BookingResponse bookTraining(@Nonnull final BookingRequest bookingRequest) {
    final BookingResponse response = new BookingResponse();
    response.setResponseCode(GENERAL_FAIL);
    final boolean success = trainingsDAO
        .saveTrainingReservation(bookingRequest.getTrainingId(), bookingRequest.getTrainingReservation());
    if(success) {
      response.setResponseCode(SUCCESS);
    }
    return response;
  }

  public boolean removeTrainingInstance(@Nonnull final User user, @Nonnull final String instanceId) {
    List<Training> userTrainings = trainingsDAO.getTrainings(null, null, null, user.getId(), 0, 0, null);
    for (Training tr : userTrainings) {
      for (TrainingInstance trainingInstance : tr.getInstances()) {
        if (trainingInstance.getId().equals(instanceId)) {
          if (trainingInstance.getTrainingReservations().size() == 0) {
            trainingsDAO.deleteTrainingInstance(trainingInstance);
            return true;
          }
        }
      }
    }
    return false;
  }

  public void saveTrainingInstance(@Nonnull final String templateID, @Nonnull final Date date, final double duration) {
    final TrainingInstance trainingInstance = new TrainingInstance();
    trainingInstance.setDateStart(date);
    trainingInstance.setDateEnd(DateUtils.addHoursToDate(date, duration));
    trainingInstance.setTrainingParent(templateID);
    trainingsDAO.saveTrainingInstance(trainingInstance);
  }

  public List<Training> getUserTrainings(@Nonnull final String uuid) {
    final User loggedUser = sessionManager.getLoggedUsers().get(uuid).getUser();
    return trainingsDAO.getTrainings(null, null, null,
        loggedUser.getId(), 0, 0, null);
  }

  private List<Training> getTrainerSchedule(@Nonnull final String trainerId, @Nonnull final Date date) {
    final Date[] weekBoundaries = DateUtils.getWeekBoundariesFromDate(date);
    return trainingsDAO.getTrainings(null, weekBoundaries[0], weekBoundaries[1],
        trainerId, 0, 0, null);
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
//        String.format("Training on %s %f.0-%f.0 %s  has been reserved!", training.getDate().toString(), hours,
// minutes,
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
  public List<Training> getTrainingsByFilters(final Place city, final int range, final String categoryId,
      @Nonnull final Date dateFirst,
      @Nonnull final Date dateLast, final double maxPrice, final String sortBy, final boolean showOnline) {
    java.sql.Date sqlDateFirst = getSQLDate(getStartOfDay(dateFirst));
    java.sql.Date sqlDateLast = getSQLDate(getEndOfDay(dateLast));
    return trainingsDAO
        .getTrainings(categoryId, null, null, null, maxPrice, range, city);
  }

}
