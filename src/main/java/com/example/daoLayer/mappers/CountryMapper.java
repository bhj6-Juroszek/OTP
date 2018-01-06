package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Country;
import com.example.daoLayer.entities.Place;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-05-04.
 */
public class CountryMapper implements RowMapper<Country> {

  public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
    final Country country = new Country();
    country.setCountryId(rs.getString("countryId"));
    country.setCountryName(rs.getString("countryName"));
    return country;

  }
}
