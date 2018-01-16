package com.example.daoLayer.daos;

import com.example.backend.utils.JsonReader;
import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.*;
import com.example.daoLayer.mappers.TrainingInstanceMapper;
import com.example.daoLayer.mappers.extractors.MaterialsPathsExtractor;
import com.example.daoLayer.mappers.extractors.TrainingWithInstancesExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.daoLayer.DAOHelper.*;
import static java.sql.Types.*;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.stream.Collectors.toList;

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
    if (!tableExists(TRAININGS_TABLE_NAME)) {

      template.execute
          ("CREATE TABLE " + TRAININGS_TABLE_NAME + " (trainingsId VARCHAR (50) NOT NULL, category VARCHAR (50), " +
              "place " +

              "VARCHAR(250), price DOUBLE, lat DOUBLE, lng DOUBLE, description VARCHAR(100), details VARCHAR(500), capacity INT, owner " +
              "VARCHAR (50), PRIMARY" +
              " KEY(trainingsId)) COLLATE utf8_general_ci;"
          );
    }
    if (!tableExists(TRAININGS_INSTANCES_TABLE_NAME)) {
      template.execute
          ("CREATE TABLE " + TRAININGS_INSTANCES_TABLE_NAME + " (trainingsInsId VARCHAR (50) NOT NULL, " +
              "trainingInsDateStart DATETIME, trainingInsDateEnd DATETIME," +
              " idTrainings VARCHAR (50), PRIMARY" +
              " KEY(trainingsInsId));"
          );
    }
    if (!tableExists(TRAININGS_INSTANCES_TABLE_NAME_HISTORICAL)) {
      template.execute
          ("CREATE TABLE " + TRAININGS_INSTANCES_TABLE_NAME_HISTORICAL + " (trainingsInsId VARCHAR (50) NOT " +
              "NULL, " +
              "trainingInsDateStart DATETIME, trainingInsDateEnd DATETIME," +
              " idTrainings VARCHAR (50), PRIMARY" +
              " KEY(trainingsInsId));"
          );
    }
    if (tableExists(TRAININGS_RESERVATIONS_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + TRAININGS_RESERVATIONS_TABLE_NAME + " (trainingsResId VARCHAR (50) NOT NULL, " +
            "customerId VARCHAR (50), idTrainingsIns VARCHAR (50), PRIMARY KEY(trainingsResId));"
        );
    template.execute
        ("CREATE INDEX uniInx ON " + TRAININGS_RESERVATIONS_TABLE_NAME + " (customerId, idTrainingsIns);"
        );
  }

  public List<Training> getTrainings(@Nullable String categoryId, @Nonnull final Date from, @Nonnull final Date to) {
    return getTrainings(categoryId, from, to, null, 0.0, 0, null, null);
  }

  public List<Training> getTrainings(final long categoryId, @Nonnull final Date from, @Nonnull final Date to,
      final double maxPrice) {
    return getTrainings(categoryId, from, to, 0.0);
  }

  public List<Training> getTrainingWithInstance(@Nonnull final String instanceId) {
    return getTrainings(instanceId, null, null, null, null, 0, 0, null, null);
  }

  public List<String> getMaterials(@Nonnull final String userId) {

    final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " tr " +
        "LEFT OUTER JOIN " + TRAININGS_INSTANCES_TABLE_NAME + " trIns ON tr.trainingsId = trIns.idTrainings " +
        "INNER JOIN " + TRAININGS_RESERVATIONS_TABLE_NAME + " trRes ON (trIns.trainingsInsId = trRes" +
        ".idTrainingsIns  OR trIns.trainingsInsId = trRes.idTrainingsIns) " +
        "INNER JOIN " + CATEGORIES_TABLE_NAME + " cats ON (tr.category = cats.categoryId OR tr.category = cats" +
        ".categoryParent) " +

        "INNER JOIN " + USERS_TABLE_NAME + " usrs ON tr.owner = usrs.userId " +
        "INNER JOIN " + "(SELECT userId AS c_userId, userName AS c_userName, adress AS c_adress, mail AS c_mail, " +
        "imageUrl AS c_imageUrl FROM " + USERS_TABLE_NAME + ") usrsRes ON usrsRes.c_userId = trRes.customerId WHERE " +
        "c_userId = :userId AND EXISTS(SELECT * FROM reservationconfirmation WHERE reservationId = trRes.trainingsResId)";
    final List<Training> trainingsReservedByUser = parameterJdbcTemplate.query(SQL,
        new MapSqlParameterSource().addValue("userId", userId, VARCHAR),
        new TrainingWithInstancesExtractor());
    final List<String> materialsPaths = new ArrayList<>();
    for(Training trainingReserved:trainingsReservedByUser) {
      final String materialsSQL = "SELECT path FROM materials WHERE trainingId = :trainingId";
      materialsPaths.addAll(parameterJdbcTemplate.query(materialsSQL,
          new MapSqlParameterSource().addValue("trainingId", trainingReserved.getId(), VARCHAR),
          new MaterialsPathsExtractor()));
    }
    return materialsPaths;

  }

  public void confirmReservation(@Nonnull final String reservationId, @Nonnull final String trainerId) {
    asyncSaver.execute(() -> {
      final List<Training> ownerTrainings = getUnconfirmedReservations(trainerId);
      boolean found = false;
      for(Training training : ownerTrainings) {
        for(TrainingInstance trainingInstance:training.getInstances()) {
          for(TrainingReservation trainingReservation: trainingInstance.getTrainingReservations()) {
            if(trainingReservation.getId().equals(reservationId)) {
              found = true;
              break;
            }
          }
        }
      }
      if(found) {
        final String SQL = " INSERT INTO reservationconfirmation (reservationId) VALUES (?) ";
        template.update(SQL, reservationId);
      }
    });
  }

  public List<Training> getUnconfirmedReservations(@Nonnull final String trainerId) {
    final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " tr " +
        "LEFT OUTER JOIN " + TRAININGS_INSTANCES_TABLE_NAME + " trIns ON tr.trainingsId = trIns.idTrainings " +
        "INNER JOIN " + TRAININGS_RESERVATIONS_TABLE_NAME + " trRes ON (trIns.trainingsInsId = trRes" +
        ".idTrainingsIns  OR trIns.trainingsInsId = trRes.idTrainingsIns) " +
        "INNER JOIN " + CATEGORIES_TABLE_NAME + " cats ON (tr.category = cats.categoryId OR tr.category = cats" +
        ".categoryParent) " +

        "INNER JOIN " + USERS_TABLE_NAME + " usrs ON tr.owner = usrs.userId " +
        "INNER JOIN " + "(SELECT userId AS c_userId, userName AS c_userName, adress AS c_adress, mail AS c_mail, " +
        "imageUrl AS c_imageUrl FROM " + USERS_TABLE_NAME + ") usrsRes ON usrsRes.c_userId = trRes.customerId WHERE " +
        "tr.owner = :trainerId AND NOT EXISTS(SELECT * FROM reservationconfirmation WHERE reservationId = trRes.trainingsResId) ";
    return parameterJdbcTemplate.query(SQL,
        new MapSqlParameterSource().addValue("trainerId", trainerId, VARCHAR),
        new TrainingWithInstancesExtractor());
  }

  public List<Training> getTrainings(@Nullable final String categoryId, @Nullable final Date fromDate,
      @Nullable final Date toDate,
      @Nullable final String trainerId, final double maxPrice, final int maxDistance,
      @Nullable final Place placeAround, @Nullable String traineeId) {
    return getTrainings(null, categoryId, fromDate, toDate, trainerId, maxPrice, maxDistance, placeAround, traineeId);
  }

  public void addMaterialToTraining(@Nonnull final String trainingId, @Nonnull final String path, @Nonnull final String ownerId) {
    asyncSaver.execute(() -> {
      final String SQL = "INSERT INTO " + "materials" + " (path, ownerId, " +
          "trainingId) VALUES " +
          "(?, ?, ?) ";
      template.update(SQL, path, ownerId, trainingId);
    });
  }

  public List<Training> getUpcomingTrainings(@Nonnull final User user, @Nonnull final Date dateTo) {
    final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " tr " +
        "LEFT OUTER JOIN " + TRAININGS_INSTANCES_TABLE_NAME + " trIns ON tr.trainingsId = trIns.idTrainings " +
        "INNER JOIN " + TRAININGS_RESERVATIONS_TABLE_NAME + " trRes ON (trIns.trainingsInsId = trRes" +
        ".idTrainingsIns  OR trIns.trainingsInsId = trRes.idTrainingsIns) " +
        "INNER JOIN " + CATEGORIES_TABLE_NAME + " cats ON (tr.category = cats.categoryId OR tr.category = cats" +
        ".categoryParent) " +
        "INNER JOIN " + USERS_TABLE_NAME + " usrs ON tr.owner = usrs.userId " +
        "INNER JOIN " + "(SELECT userId AS c_userId, userName AS c_userName, adress AS c_adress, mail AS c_mail, " +
        "imageUrl AS c_imageUrl FROM " + USERS_TABLE_NAME + ") usrsRes ON usrsRes.c_userId = trRes.customerId WHERE " +
        "c_userId = :userId " +
        "AND trIns.trainingInsDateStart < :dateTo";
    return parameterJdbcTemplate.query(SQL,
        new MapSqlParameterSource().addValue("userId", user.getId(), VARCHAR).addValue("dateTo", dateTo, TIMESTAMP),
        new TrainingWithInstancesExtractor());
  }

  public List<Training> getTrainingsToRate(@Nonnull final String userId) {
    final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " tr " +
        "LEFT OUTER JOIN " + TRAININGS_INSTANCES_TABLE_NAME_HISTORICAL + " trIns ON tr.trainingsId = trIns.idTrainings " +
        "INNER JOIN " + TRAININGS_RESERVATIONS_TABLE_NAME + " trRes ON (trIns.trainingsInsId = trRes" +
        ".idTrainingsIns  OR trIns.trainingsInsId = trRes.idTrainingsIns) " +
        "INNER JOIN " + CATEGORIES_TABLE_NAME + " cats ON (tr.category = cats.categoryId OR tr.category = cats" +
        ".categoryParent) " +
        "INNER JOIN " + USERS_TABLE_NAME + " usrs ON tr.owner = usrs.userId " +
        "INNER JOIN " + "(SELECT userId AS c_userId, userName AS c_userName, adress AS c_adress, mail AS c_mail, " +
        "imageUrl AS c_imageUrl FROM " + USERS_TABLE_NAME + ") usrsRes ON usrsRes.c_userId = trRes.customerId WHERE " +
        "c_userId = :userId AND NOT EXISTS " +
        "(SELECT * FROM " + RATES_TABLE_NAME + " WHERE ratedUserId = tr.owner AND ratingUserId = :userId )";
    return parameterJdbcTemplate.query(SQL, new MapSqlParameterSource().addValue("userId", userId, VARCHAR),
        new TrainingWithInstancesExtractor());
  }

  private List<Training> getTrainings(@Nullable final String instanceId, @Nullable final String categoryId,
      @Nullable final Date fromDate,
      @Nullable final Date toDate,
      @Nullable final String trainerId, final double maxPrice, final int maxDistance,
      @Nullable final Place placeAround, @Nullable String traineeId) {
    final StringBuilder SQL = new StringBuilder();
    final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    SQL.append("SELECT * FROM " + TRAININGS_TABLE_NAME + " tr " +
        "LEFT OUTER JOIN " + TRAININGS_INSTANCES_TABLE_NAME + " trIns ON tr.trainingsId = trIns.idTrainings " +
        "LEFT OUTER JOIN " + TRAININGS_RESERVATIONS_TABLE_NAME + " trRes ON (trIns.trainingsInsId = trRes" +
        ".idTrainingsIns OR trIns.trainingsInsId = trRes.idTrainingsIns) " +
        "INNER JOIN " + CATEGORIES_TABLE_NAME + " cats ON (tr.category = cats.categoryId OR tr.category = cats" +
        ".categoryParent) " +
        "INNER JOIN " + USERS_TABLE_NAME + " usrs ON tr.owner = usrs.userId " +
        "LEFT OUTER JOIN " + "(SELECT userId AS c_userId, userName AS c_userName, adress AS c_adress, mail AS c_mail," +
        " " +
        "imageUrl AS c_imageUrl FROM " + USERS_TABLE_NAME + ") usrsRes ON usrsRes.c_userId = trRes.customerId ");
    applyInstanceIdFilter(SQL, parameterSource, instanceId);
    applyTrainerAndTraineeFilters(SQL, parameterSource, traineeId, trainerId);
    applyMaxPriceFilter(SQL, parameterSource, maxPrice);
    applyCategoryFilter(SQL, parameterSource, categoryId);
    applyDateFilter(SQL, parameterSource, fromDate, toDate);
    return applyDistanceFilter(parameterJdbcTemplate.query(SQL.toString(),
        parameterSource,
        new TrainingWithInstancesExtractor()), maxDistance, placeAround);
  }

  private void applyInstanceIdFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, @Nullable final String id) {
    if (id != null) {
      resolveWhereOrAndQuery(builder);
      builder.append(" (trIns.trainingsInsId = :trainingsInsId) ");
      parameterSource.addValue("trainingsInsId", id, VARCHAR);
    }
  }

  private void applyTrainerAndTraineeFilters(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, @Nullable final String traineeId,
      @Nullable final String trainerId) {
    if (trainerId != null) {
      resolveWhereOrAndQuery(builder);
      if (traineeId != null) {
        builder.append(" ( ");
        applyTrainerFilter(builder, parameterSource, trainerId);
        builder.append(" OR ");
        applyTraineeFilter(builder, parameterSource, traineeId);
        builder.append(" ) ");
      } else {
        applyTrainerFilter(builder, parameterSource, trainerId);
      }
    } else if (traineeId != null) {
      resolveWhereOrAndQuery(builder);
      applyTraineeFilter(builder, parameterSource, traineeId);
    }
  }

  private void applyTraineeFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, @Nullable final String traineeId) {
    if (!(traineeId == null)) {
      builder.append(" trRes.customerId = :traineeId ");
      parameterSource.addValue("traineeId", traineeId, VARCHAR);
    }
  }

  private void applyDateFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, @Nullable final Date dateFrom,
      @Nullable final Date dateTo) {
    Date dateFromAndBeforeNow = dateFrom;
    // id dateBefore is invalid, all old instances records should be moved to historical table
    if (dateFrom != null && new Date().after(dateFrom)) {
      dateFromAndBeforeNow = new Date();

      resolveWhereOrAndQuery(builder);
      builder.append(" ( (trIns.trainingInsDateStart >= :fromDate ");
      parameterSource.addValue("fromDate", dateFromAndBeforeNow, TIMESTAMP);

      if (dateTo != null) {
        builder.append(" AND trIns.trainingInsDateStart <= :toDate) OR cats.theoretical = 1) ");
        parameterSource.addValue("toDate", dateTo, TIMESTAMP);
      } else {
        builder.append(") OR cats.theoretical = 1) ");
      }
    }
  }

  private void applyCategoryFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, @Nullable final String categoryId) {
    if (categoryId != null) {
      resolveWhereOrAndQuery(builder);
      builder.append(" (cats.categoryId = :categoryId OR cats.categoryParent = :categoryId) ");
      parameterSource.addValue("categoryId", categoryId, VARCHAR);
    }
  }

  private void applyTrainerFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, @Nullable final String trainerId) {
    if (trainerId != null) {
      builder.append(" usrs.userId = :trainerId ");
      parameterSource.addValue("trainerId", trainerId, VARCHAR);
    }
  }

  private void applyMaxPriceFilter(@Nonnull final StringBuilder builder,
      @Nonnull final MapSqlParameterSource parameterSource, final double maxPrice) {
    if (maxPrice > 0.0) {
      resolveWhereOrAndQuery(builder);
      builder.append(" tr.price <= :maxPrice ");
      parameterSource.addValue("maxPrice", maxPrice, DOUBLE);
    }
  }

  private void resolveWhereOrAndQuery(@Nonnull final StringBuilder builder) {
    final String whereOrAnd = (builder.toString().contains("WHERE")) ? " AND" : " WHERE";
    builder.append(whereOrAnd);
  }

  private List<Training> applyDistanceFilter(@Nonnull final List<Training> trainings, final int maxDistance,
      @Nullable final Place place) {
    if (maxDistance == 0 || place == null) {
      return trainings;
    }
    final JsonReader reader = new JsonReader();
    return trainings.stream().filter(
        training -> reader.distance(place, training.getPlace()) <= maxDistance || training.getCategory()
            .getTheoretical()).collect(
        toList());
  }

  public void saveTraining(@Nonnull final Training training) {
    asyncSaver.execute(() -> {
      final Place place = training.getPlace();
      final Category category = training.getCategory();
      final User owner = training.getOwner();
      if (place != null && category != null && owner != null) {
        final String SQL = "INSERT INTO " + TRAININGS_TABLE_NAME + " (trainingsId, category, place, price, " +
            "lat, lng, " +
            "description, details, capacity, owner) VALUES (:trainingsId, :category, :place, :price, :lat, :lng, :description, :details," +
            " " +
            ":capacity, :owner) ";
        try {
          parameterJdbcTemplate.update(SQL,
              new MapSqlParameterSource().addValue("trainingsId", training.getId(), VARCHAR)
                  .addValue("place", place.getName(), VARCHAR)
                  .addValue("price", training.getPrice(), DOUBLE)
                  .addValue("lat", place.getLat(), DOUBLE)
                  .addValue("lng", place.getLng(), DOUBLE)
                  .addValue("description", training.getDescription(), VARCHAR)
                  .addValue("details", training.getDetails(), VARCHAR)
                  .addValue("capacity", training.getCapacity(), NUMERIC)
                  .addValue("owner", owner.getId(), VARCHAR)
                  .addValue("category", category.getId(), VARCHAR));
        } catch (Exception e) {
          LOGGER.error(e);
        }
      }
    });
  }

  public void saveTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(() -> {
      final String SQL =
          "INSERT INTO " + TRAININGS_INSTANCES_TABLE_NAME + "(trainingsInsId, idTrainings, " +
              "trainingInsDateStart, " +
              "trainingInsDateEnd) " +
              "SELECT * FROM (SELECT :trainingsInsId AS trainingsInsId, :idTrainings AS idTrainings, " +
              ":trainingInsDateStart AS trainingInsDateStart, :trainingInsDateEnd AS trainingInsDateEnd) AS " +
              "tmp " +
              "WHERE NOT EXISTS (" +
              "    SELECT idTrainings, trainingInsDateStart, trainingInsDateEnd   FROM " +
              TRAININGS_INSTANCES_TABLE_NAME + " WHERE idTrainings = :idTrainings AND (" +
              "(trainingInsDateStart BETWEEN :trainingInsDateStart AND " +
              ":trainingInsDateEnd) OR " +
              "(trainingInsDateEnd BETWEEN :trainingInsDateStart AND :trainingInsDateEnd))" +
              ") LIMIT 1";
      try {
        parameterJdbcTemplate.update(SQL,
            new MapSqlParameterSource().addValue("idTrainings", trainingInstance.getTrainingParent(), VARCHAR)
                .addValue("trainingsInsId", trainingInstance.getId(), VARCHAR)
                .addValue("trainingInsDateStart", trainingInstance.getDateStart(), TIMESTAMP)
                .addValue("trainingInsDateEnd", trainingInstance.getDateEnd(), TIMESTAMP));
      } catch (Exception e) {
        LOGGER.error(e);
      }
    });
  }

  public Training saveTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    final Training training = getTrainingByInstanceId(trainingReservation.getTrainingInstance());

    if (training == null || training.getInstances().size() > 1) {
      return null;
    }
    final TrainingInstance trainingInstanceFound = training.getInstances().get(0);
    if (training.getCapacity() > trainingInstanceFound.getTrainingReservations().size()) {
      final User customer = trainingReservation.getCustomer();
      if (customer != null) {
        final String SQL = "INSERT INTO " + TRAININGS_RESERVATIONS_TABLE_NAME + " (trainingsResId, customerId, " +
            "idTrainingsIns) VALUES " +
            "(?, ?, ?) ";
        template.update(SQL, trainingReservation.getId(), customer.getId(), trainingReservation.getTrainingInstance());
        trainingInstanceFound.getTrainingReservations().add(trainingReservation);
        return training;
      }
    }
    return null;
  }

  public void updateTraining(@Nonnull final String ownerId, final double price, @Nonnull final String description,
      @Nonnull final String details, @Nonnull final String trainingId) {
    final String SQL = "UPDATE " + TRAININGS_TABLE_NAME + " SET   price = ?, " +
        "description = ?, details = ? WHERE trainingsId= ? AND owner = ?";
    template.update(SQL, price, description, details, trainingId, ownerId );
  }

  public void updateTraining(@Nonnull final Training training) {
    asyncSaver.execute(() -> {
      final Place place = training.getPlace();
      final Category category = training.getCategory();
      final String SQL = "UPDATE " + TRAININGS_TABLE_NAME + " SET category = ?, place = ?, price = ?, " +
          "lat = ?, lng = ?, " +
          "description = ?, size = ? WHERE trainingsId= ?";
      template.update(SQL, category.getId(), place.getName(), training.getPrice(), place.getLat(),
          place.getLng(), training.getDescription(), training.getCapacity(), training.getId());
    });
  }

  public void updateTrainingInstance(@Nonnull final TrainingInstance trainingInstance) {
    asyncSaver.execute(() -> {
      final String SQL = "UPDATE " + TRAININGS_INSTANCES_TABLE_NAME + " SET trainingInsDateStart = ?, idTrainings = " +
          " ? " +
          "WHERE trainingsInsId = ?";
      template
          .update(SQL, trainingInstance.getDateStart(), trainingInstance.getTrainingParent(), trainingInstance.getId());
    });

  }

  public void updateTrainingReservation(@Nonnull final TrainingReservation trainingReservation) {
    asyncSaver.execute(() -> {
      final User customer = trainingReservation.getCustomer();
      if (customer != null) {
        final String SQL = "UPDATE " + TRAININGS_RESERVATIONS_TABLE_NAME + " SET customerId = ?, idTrainingsIns = ? " +
            "WHERE trainingsResId = ?";
        template.update(SQL, customer.getId(), trainingReservation.getTrainingInstance(), trainingReservation.getId());
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

  public void moveToHistorical(final int daysBefore) {
    asyncSaver.execute(() -> {
      final Date date = new Date();
      final java.sql.Date sqlDate = new java.sql.Date(date.getTime() - DAYS.toMillis(daysBefore));
      String SQL = "INSERT INTO " + TRAININGS_INSTANCES_TABLE_NAME_HISTORICAL + " SELECT * FROM " +
          TRAININGS_INSTANCES_TABLE_NAME + " WHERE trainingInsDateStart < ?";
      template.update(SQL, sqlDate);
      SQL = "DELETE FROM " + TRAININGS_INSTANCES_TABLE_NAME + " WHERE trainingInsDateStart < ?";
      template.update(SQL, sqlDate);
    });
  }

  @Nullable
  public TrainingInstance getTrainingInstanceById(@Nonnull final String id) {
    try {
      final String SQL = "SELECT * FROM " + TRAININGS_INSTANCES_TABLE_NAME + " WHERE trainingsInsId = ?";
      return template.queryForObject(SQL,
          new Object[]{id}, new TrainingInstanceMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public Training getTrainingByInstanceId(@Nonnull final String id) {
    List<Training> trainings = getTrainingWithInstance(id);
    if (trainings.isEmpty() || trainings.size() > 1) {
      return null;
    }
    return trainings.get(0);
  }
}





