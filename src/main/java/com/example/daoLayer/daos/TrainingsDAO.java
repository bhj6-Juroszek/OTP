package com.example.daoLayer.daos;

import com.example.backend.model.JsonReader;
import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.*;
import com.example.daoLayer.mappers.TrainingMapper;
import com.example.daoLayer.mappers.TrainingWithInstancesExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.daoLayer.DAOHelper.*;
import static java.util.concurrent.TimeUnit.DAYS;

/**
 * Created by Bartek on 2017-03-25.
 */
@Repository
public class TrainingsDAO extends DAO {

  private static final Logger LOGGER = LogManager.getLogger(TrainingsDAO.class);

  public TrainingsDAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    super(template, asyncDbSaver);
  }

  @Override
  public void createTable() {
    if (tableExists(TRAININGS_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + TRAININGS_TABLE_NAME + " (trainingsId VARCHAR (50) NOT NULL, category VARCHAR (50), place " +
            "VARCHAR(250), price DOUBLE, lat DOUBLE, lng DOUBLE, description VARCHAR(500), capacity INT, owner " +
            "VARCHAR (50), PRIMARY" +
            " KEY(trainingsId)) COLLATE utf8_general_ci;"
        );
    if (tableExists(TRAININGS_INSTANCES_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + TRAININGS_INSTANCES_TABLE_NAME + " (trainingsInsId VARCHAR (50) NOT NULL, " +
            "trainingInsDateStart DATE, trainingInsDateEnd DATE," +
            " idTrainings VARCHAR (50), PRIMARY" +
            " KEY(trainingsInsId));"
        );
    if (tableExists(TRAININGS_RESERVATIONS_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + TRAININGS_RESERVATIONS_TABLE_NAME + " (trainingsResId VARCHAR (50) NOT NULL, " +
            "customerId VARCHAR (50), idTrainingsIns VARCHAR (50), PRIMARY KEY(trainingsResId));"
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

  public void saveTraining(@Nonnull final Training training) {
    asyncSaver.execute(() -> {
      final Place place = training.getPlace();
      final Category category = training.getCategory();
      final User owner = training.getOwner();
      if (place != null && category != null && owner != null) {
        final String SQL = "INSERT INTO " + TRAININGS_TABLE_NAME + " (trainingsId, category, place, price, lat, lng, " +
            "description, capacity, owner) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
          template.update(SQL, training.getId(), category.getId(), place.getName(), training.getPrice(), place.getLat(),
              place.getLng(), training.getDescription(), training.getCapacity(), training.getOwner().getId());
        } catch(Exception e) {
          LOGGER.error(e);
        }
      }
    });
  }

  public void saveTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(() -> {
      final String SQL = "MERGE INTO" + TRAININGS_INSTANCES_TABLE_NAME + " USING dual " +
          "            ON (idTrainings = :idTrainings AND ((trainingInsDateStart BETWEEN :trainingInsDateStart AND " +
          ":trainingInsDateEnd) OR " +
          "(trainingInsDateEnd BETWEEN :trainingInsDateStart AND :trainingInsDateEnd))) " +
          "WHEN NOT MATCHED THEN INSERT INTO " + TRAININGS_INSTANCES_TABLE_NAME + "(idTrainings, " +
          "trainingInsDateStart, " +
          "trainingInsDateEnd) " +
          "VALUES (:idTrainings, :trainingInsDateStart, :trainingInsDateEnd)";
      parameterJdbcTemplate.update(SQL,
          new MapSqlParameterSource().addValue("idTrainings", trainingInstance.getTrainingParent(), Types.NUMERIC)
              .addValue("trainingInsDateStart", trainingInstance.getDateStart(), Types.TIMESTAMP)
              .addValue("trainingInsDateEnd", trainingInstance.getDateEnd(), Types.TIMESTAMP));
    });
  }

  public void saveTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    asyncSaver.execute(() -> {
      final User customer = trainingReservation.getCustomer();
      if (customer != null) {
        final String SQL = "INSERT INTO " + TRAININGS_RESERVATIONS_TABLE_NAME + " (customerId, idTrainingIns) VALUES " +
            "(?, ?)";
        template.update(SQL, customer.getId(), trainingReservation.getTrainingInstance());
      }
    });
  }

  public void updateTraining(@Nonnull final Training training) {
    asyncSaver.execute(() -> {
      final Place place = training.getPlace();
      final Category category = training.getCategory();
      final String SQL = "UPDATE " + TRAININGS_TABLE_NAME + " SET (category = ?, place = ?, price = ?, " +
          "lat = ?, lng = ?, " +
          "description = ?, size = ?, WHERE trainingsId= ?";
      template.update(SQL, category.getId(), place.getName(), training.getPrice(), place.getLat(),
          place.getLng(), training.getDescription(), training.getCapacity(), training.getId());
    });
  }

  public void updateTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(() -> {
      final String SQL = "UPDATE " + TRAININGS_INSTANCES_TABLE_NAME + " SET (trainingInsDateStart = ?, idTrainings = " +
          "?)" +
          " " +
          "WHERE trainingsInsId = ?";
      template
          .update(SQL, trainingInstance.getDateStart(), trainingInstance.getTrainingParent(), trainingInstance.getId());
    });

  }

  public void updateTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    asyncSaver.execute(() -> {
      final User customer = trainingReservation.getCustomer();
      if (customer != null) {
        final String SQL = "UPDATE " + TRAININGS_RESERVATIONS_TABLE_NAME + " SET (customerId, idTrainingIns) VALUES " +
            "(?, ?)";
        template.update(SQL, customer.getId(), trainingReservation.getTrainingInstance());
      }
    });

  }

  public void deleteTraining(@Nonnull final Training training) {
    asyncSaver.execute(() -> {
      final String SQL = "DELETE FROM " + TRAININGS_TABLE_NAME + " WHERE trainingsId = ? ";
      template.update(SQL, training.getId());
    });
  }

  public void deleteTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(() -> {
      final String SQL = "DELETE FROM " + TRAININGS_INSTANCES_TABLE_NAME + " WHERE trainingsInsId = ? ";
      template.update(SQL, trainingInstance.getId());
    });
  }

  public void deleteTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    asyncSaver.execute(() -> {
      final String SQL = "DELETE FROM " + TRAININGS_RESERVATIONS_TABLE_NAME + " WHERE trainingsResId = ? ";
      template.update(SQL, trainingReservation.getId());
    });
  }

  public void clear(final int daysBefore) {
    asyncSaver.execute(() -> {
      final Date date = new Date();
      final java.sql.Date sqlDate = new java.sql.Date(date.getTime() - DAYS.toMillis(daysBefore));
      final String SQL = "DELETE FROM " + TRAININGS_INSTANCES_TABLE_NAME + " WHERE trainingInsDateStart < ?";
      template.update(SQL, sqlDate);
    });
  }

  @Nullable
  public Training getTrainingById(@Nonnull final String id) {
    try {
      final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " WHERE trainingsId = ?";
      return template.queryForObject(SQL,
          new Object[]{id}, new TrainingMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }
}





