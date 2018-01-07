package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.Place;
import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-03-23.
 */
public class TrainingMapper implements RowMapper<Training> {

  public Training mapRow(ResultSet rs, int rowNum) throws SQLException {
    final UserMapper userMapper = new UserMapper();
    final CategoryMapper categoryMapper = new CategoryMapper();
    final Training result = new Training();
    result.setId(rs.getString("trainingsId"));
    result.setDescription(rs.getString("description"));
    result.setPrice(rs.getDouble("price"));
    result.setCapacity(rs.getInt("capacity"));

    final Place place = new Place();
    place.setName(rs.getString("place"));
    place.setLat(rs.getDouble("lat"));
    place.setLng(rs.getDouble("lng"));
    result.setPlace(place);
    final Category category = categoryMapper.mapRow(rs, rowNum);
    result.setCategory(category);
    final User owner = userMapper.mapRow(rs, rowNum);
    owner.setPassword(null).setLogin(null).setConfirmation(null);
    result.setOwner(owner);
    return result;
  }
}
