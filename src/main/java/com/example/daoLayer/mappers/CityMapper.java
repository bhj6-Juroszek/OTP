package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.City;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-05-04.
 */
public class CityMapper implements RowMapper<City> {

    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
        City cit = new City();
        cit.setId(rs.getInt("id"));
        cit.setName(rs.getString("name"));
        cit.setCountryId(rs.getInt("country"));

        return cit;

    }
}
