package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Rate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Created by Bartek on 2017-03-10.
 */
public class RateMapper implements RowMapper<Optional<Rate>> {

  public Optional<Rate> mapRow(ResultSet rs, int rowNum) throws SQLException {
    final String id = rs.getString("rateId");
    if (id == null) {
      return empty();
    }
    final Rate rate = new Rate();
    rate.setId(id);
    rate.setComment(rs.getString("rateComment"));
    rate.setValue(rs.getInt("rateValue"));
    rate.setToId(rs.getString("ratedUserId"));
    rate.setFromId(rs.getString("ratingUserId"));
    rate.setDate(rs.getDate("rateDate"));
    return of(rate);
  }
}
