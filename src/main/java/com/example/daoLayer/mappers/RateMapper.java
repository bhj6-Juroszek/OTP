package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Rate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-03-10.
 */
public class RateMapper implements RowMapper<Rate> {

  public Rate mapRow(ResultSet rs, int rowNum) throws SQLException {
    final Rate rate = new Rate();
    rate.setId(rs.getString("rateId"));
    rate.setComment(rs.getString("rateComment"));
    rate.setValue(rs.getInt("rateValue"));
    rate.setToId(rs.getString("ratedUserId"));
    rate.setFromId(rs.getString("ratingUserId"));
    rate.setDate(rs.getDate("rateDate"));
    return rate;
  }
}
