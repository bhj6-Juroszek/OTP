package com.example.daoLayer.daos;

import com.example.model.JsonReader;
import com.example.model.Place;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.Training;
import com.example.daoLayer.mappers.TrainingMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Date;
import java.util.ArrayList;

import static com.example.backend.utils.DateUtils.addHoursToDate;
import static com.example.daoLayer.DAOHandler.TRAININGS_TABLE_NAME;
import static com.example.model.JsonReader.*;

/**
 * Created by Bartek on 2017-03-25.
 */
@Repository
public class TrainingsDAO extends DAO {

  public TrainingsDAO(@Nonnull final JdbcTemplate template) {
    super(template);

  }

  @Override
  public void createTable() {
    template.execute
        ("CREATE TABLE " + TRAININGS_TABLE_NAME + " (id INT NOT NULL AUTO_INCREMENT, date DATE, length DOUBLE, " +
            "price INT, category INT, city VARCHAR(50), description VARCHAR(500), ownerId INT, takenById INT, PRIMARY" +
            " KEY(id));"
        );
  }

  public boolean saveToDB(@Nonnull final Training training) {
    final String SQL = "INSERT INTO " + TRAININGS_TABLE_NAME + " (date, length, price, category, city, description, " +
        "ownerId, " +
        "takenById) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    if (!exists(training)) {
      template.update(SQL, training.getDate(), training.getLength(), training.getPrice(), training.getCategory(),
          training.getCity(), training.getDescription(), training.getOwnerId(), training.getTakenById());
    } else {
      return false;
    }
    return true;
  }

  public boolean updateRecord(@Nonnull final Training training) {
    if (existsById(training)) {
      final String SQL = "UPDATE " + TRAININGS_TABLE_NAME + " SET date = ?, length = ?, price = ?, category = ?, city" +
          " =" +
          " ?,description = ?,  ownerId= ?, takenById= ?  WHERE id= ?;";
      template.update(SQL, training.getDate(), training.getLength(), training.getPrice(), training.getCategory(),
          training.getCity(), training.getDescription(), training.getOwnerId(), training.getTakenById(),
          training.getId());
      return true;
    }
    return false;

  }

  private boolean existsById(@Nonnull final Training training) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + TRAININGS_TABLE_NAME + " WHERE id = ?  ", Integer.class, training.getId());
    return cnt != null && cnt > 0;
  }

  private boolean exists(@Nonnull final Training training) {
    for (Training tr : this.getTrainingsFromDate(training.getOwnerId(), training.getDate())) {
      if (tr.getDate().before(training.getDate()) && tr.getDate()
          .before(addHoursToDate(training.getDate(), training.getLength()))) {
        return true;
      }
      if (addHoursToDate(tr.getDate(), tr.getLength()).after(training.getDate()) && addHoursToDate(tr.getDate(),
          tr.getLength()).before(addHoursToDate(training.getDate(), training.getLength()))) {
        return true;
      }
      if (tr.getDate() == training.getDate()) {
        return true;
      }
    }
    return false;
  }

  public void delete(@Nonnull final Training training) {
    final String SQL = "DELETE FROM " + TRAININGS_TABLE_NAME + " WHERE id = ? ";
    template.update(SQL, training.getId());
  }

  public void clear() {
    final java.util.Date date = new java.util.Date();
    final java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    final String SQL = "DELETE FROM " + TRAININGS_TABLE_NAME + " WHERE date < ?";
    template.update(SQL, sqlDate);
  }

  @Nullable
  public Training getTrainingById(@Nonnull final Long id) {
    try {
      final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " WHERE id = ?";
      return template.queryForObject(SQL,
          new Object[]{id}, new TrainingMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public ArrayList<Training> getTrainingsFromDate(@Nonnull final Long fromId, @Nonnull final Date date) {
    try {
      final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " WHERE ownerId = ? AND year(date) = year (?) AND " +
          "month(date) = month(?) AND day(date) = day(?) ";
      return (ArrayList<Training>) template.query(SQL,
          new Object[]{fromId, date, date, date}, new RowMapperResultSetExtractor<Training>(new TrainingMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public ArrayList<Training> getTrainingsFromDate(@Nonnull final Date date) {
    try {
      final String SQL = "SELECT * FROM " + TRAININGS_TABLE_NAME + " WHERE date = ? ";
      return (ArrayList<Training>) template.query(SQL,
          new Object[]{date}, new RowMapperResultSetExtractor<>(new TrainingMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public ArrayList<Training> getTrainingsByFilter(@Nonnull final String cityName, final double range,
      @Nonnull final Category cat, final Date dateFirst,
      @Nonnull final Date dateLast, final double maxPrice, final String sortBy, final boolean showOnline) {
    final ArrayList<Training> result = new ArrayList<>();
    try {
      Object[] list = null;
      String SQL = "";
      if (dateFirst == null && maxPrice == 0) {
        SQL = "select * from " + TRAININGS_TABLE_NAME + "where category = ? AND takenById = 0 ORDER BY ";
        list = new Object[]{cat.getId()};
      } else if (dateFirst == null && maxPrice != 0) {
        SQL = "select * from " + TRAININGS_TABLE_NAME + " where category = ? AND takenById = 0 AND price < ?  ORDER " +
            "BY ";
        list = new Object[]{cat.getId(), maxPrice};
      } else if (dateFirst != null && maxPrice == 0) {
        SQL = "select * from " + TRAININGS_TABLE_NAME + " where category = ? AND takenById = 0 AND date BETWEEN ? AND" +
            " ?" +
            "  ORDER BY ";
        list = new Object[]{cat.getId(), dateFirst, dateLast};
      } else if (dateFirst != null && maxPrice != 0) {
        SQL = "select * from " + TRAININGS_TABLE_NAME + " where category = ? AND takenById = 0 AND date BETWEEN ? AND" +
            " ?" +
            " AND price < ? ORDER BY ";
        list = new Object[]{cat.getId(), dateFirst, dateLast, maxPrice};
      }

      if (sortBy != null) {
        SQL += sortBy;
      } else {
        SQL += "DATE";
      }
      final JsonReader reader = new JsonReader();
      final ArrayList<Training> trainings = (ArrayList<Training>) template.query(SQL,
          list, new RowMapperResultSetExtractor<>(new TrainingMapper()));
      if (range != 0) {
        Place place1 = null;
        Place place2;
        place2 = getCity(cityName);

        for (int i = 0; i < trainings.size(); i++) {
          if (trainings.get(i).getCity().equals("")) {
            if (showOnline) {
              result.add(trainings.get(i));
            }
          }
          if (i > 0 && !trainings.get(i).getCity().equals(trainings.get(i - 1).getCity())) {
            place1 = getCity(trainings.get(i).getCity());
          }
          if (place1 != null) {
            if (reader.distance(place1, place2) < range) {
              result.add(trainings.get(i));
            }
          }
        }
      } else {
        for (int i = 0; i < trainings.size(); i++) {
          if (trainings.get(i).getCity().equals("")) {
            if (showOnline) {
              result.add(trainings.get(i));
            }
          } else {
            result.add(trainings.get(i));
          }
        }
      }
    } catch (EmptyResultDataAccessException ignored) {
    }

    return result;
  }
}





