package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Rate;
import com.example.daoLayer.mappers.RateMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.example.daoLayer.DAOHelper.PROFILES_TABLE_NAME;
import static com.example.daoLayer.DAOHelper.RATES_TABLE_NAME;

/**
 * Created by Bartek on 2017-03-10.
 */
@Repository
public class RatesDAO extends DAO {

  RatesDAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    super(template, asyncDbSaver);
  }

  @Override
  public void createTable() {
    if (tableExists(RATES_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + RATES_TABLE_NAME + " (rateId VARCHAR(50) NOT NULL, rateComment VARCHAR(250), rateValue " +
            "INT, " +
            "ratedUserId INT, ratingUserId INT, rateDate DATE, PRIMARY KEY(rateId));"
        );
  }

  public boolean saveToDB(@Nonnull final Rate rate) {
    final String SQL = "INSERT INTO " + RATES_TABLE_NAME + " (rateId, rateComment, rateValue, ratedUserId, " +
        "ratingUserId, rateDate) VALUES (?, ?, ?, ?," +
        " ?)";
    if (!exists(rate)) {
      template.update(SQL, rate.getId(), rate.getComment(), rate.getValue(), rate.getToId(), rate.getFromId(),
          rate.getDate());
    } else {
      return false;
    }
    return true;
  }

  private boolean exists(@Nonnull final Rate rate) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + RATES_TABLE_NAME + " WHERE ratedUserId = ? AND ratingUserId = ? ", Integer.class,
        rate.getToId(), rate.getFromId());
    return cnt != null && cnt > 0;
  }

  public void delete(@Nonnull final Rate rate) {

    final String SQL = "DELETE FROM " + RATES_TABLE_NAME + " WHERE rateId = ?";
    template.update(SQL, rate.getId());
  }

  @Nullable
  public Rate getRateById(@Nonnull final String rateId) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE rateId = ?";
      return template.queryForObject(SQL,
          new Object[]{rateId}, new RateMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public List<Rate> getRatesByProfile(@Nonnull final String rateId) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE ratedUserId = ? ORDER BY rateDate DESC  ";
      return template.query(SQL,
          new Object[]{rateId}, new RowMapperResultSetExtractor<>(new RateMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return new ArrayList<>(0);
    }
  }

  public List<Rate> getRatesByFromId(@Nonnull final String rateId) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE ratingUserId = ? ORDER BY rateDate DESC   ";
      return template.query(SQL,
          new Object[]{rateId}, new RowMapperResultSetExtractor<Rate>(new RateMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return new ArrayList<>(0);
    }
  }

  @Nullable
  public Rate getProfileUserRate(@Nonnull final String ratedUserId, @Nonnull final String userId) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE ratingUserId = ? AND ratedUserId = ? ORDER BY " +
          "rateDate DESC   ";
      return template.queryForObject(SQL
          , new Object[]{userId, ratedUserId}
          , new RateMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public boolean updateRecord(@Nonnull final Rate rate) {
    if (exists(rate)) {
      final String SQL = "UPDATE " + RATES_TABLE_NAME + " SET rateComment = ?, rateValue = ?, ratedUserId = ?, formId" +
          " = ?, rateDate= ? " +
          "WHERE rateId= ?;";
      template.update(SQL, rate.getComment(), rate.getValue(), rate.getToId(), rate.getFromId(), rate.getDate(),
          rate.getId());
      return true;
    }
    return false;
  }

}
