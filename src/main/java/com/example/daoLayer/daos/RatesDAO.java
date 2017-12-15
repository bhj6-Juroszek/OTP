package com.example.daoLayer.daos;

import com.example.daoLayer.entities.Rate;
import com.example.daoLayer.mappers.RateMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.example.daoLayer.DAOHelper.RATES_TABLE_NAME;

/**
 * Created by Bartek on 2017-03-10.
 */
@Repository
public class RatesDAO extends DAO {

  public RatesDAO(@Nonnull final JdbcTemplate template) {
    super(template);
  }

  @Override
  public void createTable() {
    template.execute
        ("CREATE TABLE " + RATES_TABLE_NAME + " (id INT NOT NULL AUTO_INCREMENT, comment VARCHAR(250), value INT, " +
            "toId INT, fromId INT, date DATE, PRIMARY KEY(id));"
        );
  }

  public boolean saveToDB(@Nonnull final Rate rate) {
    final String SQL = "INSERT INTO " + RATES_TABLE_NAME + " (comment, value, toId, fromId, date) VALUES (?, ?, ?, ?," +
        " ?)";
    if (!exists(rate)) {
      template.update(SQL, rate.getComment(), rate.getValue(), rate.getToId(), rate.getFromId(), rate.getDate());
    } else {
      return false;
    }
    return true;
  }

  public boolean exists(@Nonnull final Rate rate) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + RATES_TABLE_NAME + " WHERE toId = ? AND fromId = ? ", Integer.class,
        rate.getToId(), rate.getFromId());
    return cnt != null && cnt > 0;
  }

  public void delete(@Nonnull final Rate rate) {

    final String SQL = "DELETE FROM " + RATES_TABLE_NAME + " WHERE id = ?";
    template.update(SQL, rate.getId());
  }

  @Nullable
  public Rate getRateById(@Nonnull final Long id) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE id = ?";
      return template.queryForObject(SQL,
          new Object[]{id}, new RateMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public ArrayList<Rate> getRatesByProfile(@Nonnull final Long id) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE toId = ? ORDER BY date DESC  ";
      return (ArrayList<Rate>) template.query(SQL,
          new Object[]{id}, new RowMapperResultSetExtractor<Rate>(new RateMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public ArrayList<Rate> getRatesByFromId(@Nonnull final Long id) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE fromId = ? ORDER BY date DESC   ";
      return (ArrayList<Rate>) template.query(SQL,
          new Object[]{id}, new RowMapperResultSetExtractor<Rate>(new RateMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public Rate getProfileUserRate(@Nonnull final Long toId, @Nonnull final Long userId) {
    try {
      final String SQL = "SELECT * FROM " + RATES_TABLE_NAME + " WHERE fromId = ? AND toId = ? ORDER BY date DESC   ";
      return template.queryForObject(SQL
          , new Object[]{userId, toId}
          , new RateMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public boolean updateRecord(@Nonnull final Rate rate) {
    if (exists(rate)) {
      final String SQL = "UPDATE " + RATES_TABLE_NAME + " SET comment = ?, value = ?, toId = ?, formId = ?, date= ? " +
          "WHERE id= ?;";
      template.update(SQL, rate.getComment(), rate.getValue(), rate.getToId(), rate.getFromId(), rate.getDate(),
          rate.getId());
      return true;
    }
    return false;
  }

}
