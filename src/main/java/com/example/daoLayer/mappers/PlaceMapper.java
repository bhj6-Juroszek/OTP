package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Place;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-05-04.
 */
public class PlaceMapper implements RowMapper<Place> {

  public Place mapRow(ResultSet rs, int rowNum) throws SQLException {
    final Place place = new Place();
    place.setId(rs.getString("placeId"));
    place.setName(rs.getString("placeName"));
    place.setLat(rs.getDouble("placeLat"));
    place.setLng(rs.getDouble("placeLng"));
    return place;

  }
}
