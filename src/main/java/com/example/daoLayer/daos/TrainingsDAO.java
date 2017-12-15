package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.*;
import com.example.daoLayer.mappers.TrainingMapper;
import com.example.daoLayer.mappers.TrainingWithInstancesExtractor;
import com.example.model.JsonReader;
import com.example.daoLayer.entities.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.daoLayer.DAOHelper.*;

/**
 * Created by Bartek on 2017-03-25.
 */
@Repository
public class TrainingsDAO extends DAO {

  private AsyncDbSaver asyncSaver;
  private NamedParameterJdbcTemplate parameterJdbcTemplate;

  public TrainingsDAO(@Nonnull final JdbcTemplate template) {
    super(template);
    parameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
  }

  @Override
  public void createTable() {
    template.execute
        ("CREATE TABLE " + TRAININGS_TABLE_NAME + " (trainingsId INT NOT NULL AUTO_INCREMENT, category INT, place " +
            "VARCHAR(250), price DOUBLE, lat DOUBLE, lng DOUBLE, description VARCHAR(500), capacity INT, owner " +
            "INT, PRIMARY" +
            " KEY(trainingsId));"
        );
    template.execute
        ("CREATE TABLE " + TRAININGS_INSTANCES_TABLE_NAME + " (trainingsInsId INT NOT NULL AUTO_INCREMENT, " +
            "trainingInsDateStart DATE, trainingInsDateEnd DATE," +
            " idTrainings INT, PRIMARY" +
            " KEY(trainingsInsId));"
        );
    template.execute
        ("CREATE TABLE " + TRAININGS_RESERVATIONS_TABLE_NAME + " (trainingsResId INT NOT NULL AUTO_INCREMENT, " +
            "customerId INT, idTrainingsIns INT, PRIMARY KEY(trainingsResId));"
        );
  }

  public List<Training> getTrainings(final long categoryId, @Nonnull final Date from, @Nonnull final Date to) {
    return getTrainings(categoryId, from, to, 0, 0.0, 0, null);
  }

  public List<Training> getTrainings(final long categoryId, @Nonnull final Date from, @Nonnull final Date to,
      final double maxPrice) {
    return getTrainings(categoryId, from, to, 0.0);
  }

  public List<Training> getTrainings(final long categoryId, @Nonnull final Date fromDate, @Nonnull final Date toDate,
      final long trainerId, final double maxPrice, final int maxDistance, @Nullable final Place placeAround) {
    final StringBuilder SQL = new StringBuilder();
    final MapSqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("categoryId", categoryId, Types.NUMERIC)
        .addValue("fromDate", fromDate, Types.TIMESTAMP)
        .addValue("toDate", toDate, Types.TIMESTAMP);
    SQL.append("SELECT * FROM " + TRAININGS_TABLE_NAME + " tr " +
        "LEFT OUTER JOIN " + TRAININGS_INSTANCES_TABLE_NAME + " trIns ON tr.trainingsId = trIns.idTrainings " +
        "LEFT OUTER JOIN " + TRAININGS_RESERVATIONS_TABLE_NAME + " trRes ON trIns.trainingsInsId = trRes" +
        ".idTrainingsIns " +
        "INNER JOIN " + CATEGORIES_TABLE_NAME + " cats ON tr.category = cats.categoryId " +
        "INNER JOIN " + USERS_TABLE_NAME + " usrs ON tr.owner = usrs.userId " +
        "LEFT OUTER JOIN " + "(SELECT userId AS c_userId, userName AS c_userName, adress AS c_adress, mail AS c_mail," +
        " " +
        "imageUrl AS c_imageUrl FROM " + USERS_TABLE_NAME + ") usrsRes ON usrsRes.c_userId = trRes.customerId " +
        "WHERE (cats.categoryId = :categoryId " +
        "OR cats.categoryParent = :categoryId) " +
        "AND (trIns.trainingInsDateStart BETWEEN :fromDate AND :toDate) ");
    applyTrainerFilter(SQL, parameterSource, trainerId);
    applyMaxPriceFilter(SQL, parameterSource, maxPrice);
    return applyDistanceFilter(parameterJdbcTemplate.query(SQL.toString(),
        parameterSource,
        new TrainingWithInstancesExtractor()), maxDistance, placeAround);
  }

  private void applyTrainerFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, final long trainerId) {
    if (trainerId != 0) {
      builder.append(" AND usrs.userId = :trainerId ");
      parameterSource.addValue("trainerId", trainerId, Types.NUMERIC);
    }
  }

  private void applyMaxPriceFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, final double maxPrice) {
    if (maxPrice > 0.0) {
      builder.append(" AND tr.price <= :maxPrice ");
      parameterSource.addValue("maxPrice", maxPrice, Types.DOUBLE);
    }
  }

  private List<Training> applyDistanceFilter(@Nonnull final List<Training> trainings, final int maxDistance,
      @Nullable final Place place) {
    if (maxDistance == 0 || place == null) {
      return trainings;
    }
    final List<Training> result = new ArrayList<>();
    final JsonReader reader = new JsonReader();
    for (Training training : trainings) {
      final Place trainingPlace = training.getPlace();
      if (reader.distance(place, trainingPlace) <= maxDistance) {
        result.add(training);
      }
    }
    return result;
  }

  public void saveTrainingAsynchronously(@Nonnull final Training training) {
    asyncSaver.execute(this::saveTraining, training);
  }

  public void saveTrainingInstanceAsynchronously(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(this::saveTrainingInstance, trainingInstance);
  }

  public void saveTrainingReservationAsynchronously(@Nonnull final TrainingReservation trainingReservation) {
    asyncSaver.execute(this::saveTrainingReservation, trainingReservation);
  }

  public void updateTrainingAsynchronously(@Nonnull final Training training) {
    asyncSaver.execute(this::updateTraining, training);
  }

  public void updateTrainingInstanceAsynchronously(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(this::updateTrainingInstance, trainingInstance);
  }

  public void updateTrainingReservationAsynchronously(@Nonnull final TrainingReservation trainingReservation) {
    asyncSaver.execute(this::updateTrainingReservation, trainingReservation);
  }

  public void deleteTrainingAsynchronously(@Nonnull final Training training) {
    asyncSaver.execute(this::deleteTraining, training);
  }

  public void deleteTrainingInstanceAsynchronously(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(this::deleteTrainingInstance, trainingInstance);
  }

  public void deleteTrainingReservationAsynchronously(@Nonnull final TrainingReservation trainingReservation) {
    asyncSaver.execute(this::deleteTrainingReservation, trainingReservation);
  }

  public void saveTraining(@Nonnull final Training training) {
    final Place place = training.getPlace();
    final Category category = training.getCategory();
    final User owner = training.getOwner();
    if (place != null && category != null && owner != null) {
      final String SQL = "INSERT INTO " + TRAININGS_TABLE_NAME + " (category, place, price, lat, lng, " +
          "description, capacity, owner) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      template.update(SQL, category.getId(), place.getName(), training.getPrice(), place.getLat(),
          place.getLng(), training.getDescription(), training.getCapacity(), training.getOwner().getId());
    }
  }

  private void saveTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    final String SQL = "MERGE INTO" + TRAININGS_INSTANCES_TABLE_NAME + " USING dual " +
        "            ON (idTrainings = :idTrainings AND ((trainingInsDateStart BETWEEN :trainingInsDateStart AND " +
        ":trainingInsDateEnd) OR " +
        "(trainingInsDateEnd BETWEEN :trainingInsDateStart AND :trainingInsDateEnd))) " +
        "WHEN NOT MATCHED THEN INSERT INTO " + TRAININGS_INSTANCES_TABLE_NAME + "(idTrainings, trainingInsDateStart, " +
        "trainingInsDateEnd) " +
        "VALUES (:idTrainings, :trainingInsDateStart, :trainingInsDateEnd)";
    parameterJdbcTemplate.update(SQL,
        new MapSqlParameterSource().addValue("idTrainings", trainingInstance.getTrainingParent(), Types.NUMERIC)
            .addValue("trainingInsDateStart", trainingInstance.getDateStart(), Types.TIMESTAMP)
            .addValue("trainingInsDateEnd", trainingInstance.getDateEnd(), Types.TIMESTAMP));
  }

  private void saveTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    final User customer = trainingReservation.getCustomer();
    if (customer != null) {
      final String SQL = "INSERT INTO " + TRAININGS_RESERVATIONS_TABLE_NAME + " (customerId, idTrainingIns) VALUES " +
          "(?, ?)";
      template.update(SQL, customer.getId(), trainingReservation.getTrainingInstance());
    }
  }

  private void updateTraining(@Nonnull final Training training) {
    final Place place = training.getPlace();
    final Category category = training.getCategory();
    final String SQL = "UPDATE " + TRAININGS_TABLE_NAME + " SET (category = ?, place = ?, price = ?, lat " +
        "= ?, lng = ?, " +
        "description = ?, size = ?, WHERE trainingsId= ?";
    template.update(SQL, category.getId(), place.getName(), training.getPrice(), place.getLat(),
        place.getLng(), training.getDescription(), training.getCapacity(), training.getId());
  }

  private void updateTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    final String SQL = "UPDATE " + TRAININGS_INSTANCES_TABLE_NAME + " SET (trainingInsDateStart = ?, idTrainings = ?)" +
        " " +
        "WHERE trainingsInsId = ?";
    template
        .update(SQL, trainingInstance.getDateStart(), trainingInstance.getTrainingParent(), trainingInstance.getId());
  }

  private void updateTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    final User customer = trainingReservation.getCustomer();
    if (customer != null) {
      final String SQL = "UPDATE " + TRAININGS_RESERVATIONS_TABLE_NAME + " SET (customerId, idTrainingIns) VALUES " +
          "(?, ?)";
      template.update(SQL, customer.getId(), trainingReservation.getTrainingInstance());
    }
  }

  private void deleteTraining(@Nonnull final Training training) {
    final String SQL = "DELETE FROM " + TRAININGS_TABLE_NAME + " WHERE trainingsId = ? ";
    template.update(SQL, training.getId());
  }

  private void deleteTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    final String SQL = "DELETE FROM " + TRAININGS_INSTANCES_TABLE_NAME + " WHERE trainingsInsId = ? ";
    template.update(SQL, trainingInstance.getId());
  }

  private void deleteTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    final String SQL = "DELETE FROM " + TRAININGS_RESERVATIONS_TABLE_NAME + " WHERE trainingsResId = ? ";
    template.update(SQL, trainingReservation.getId());
  }

  public void clear() {
    final Date date = new Date();
    final java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    final String SQL = "DELETE FROM " + TRAININGS_TABLE_NAME + " WHERE trainingInsDateStart < ?";
    template.update(SQL, sqlDate);
  }

  @Nullable
  public Training getTrainingById(@Nonnull final Long id) {
    try {
      final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " WHERE trainingsId = ?";
      return template.queryForObject(SQL,
          new Object[]{id}, new TrainingMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Autowired
  public void setAsyncDbSaver(@Nonnull final AsyncDbSaver asyncSaver) {
    this.asyncSaver = asyncSaver;
  }
}





