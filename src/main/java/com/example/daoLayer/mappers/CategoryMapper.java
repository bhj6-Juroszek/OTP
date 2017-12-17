package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-03-11.
 */
public class CategoryMapper implements RowMapper<Category> {

  public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
    final Category category = new Category();
    category.setId(rs.getString("categoryId"));
    category.setName(rs.getString("categoryName"));
    category.setParent(rs.getInt("categoryParent"));

    return category;

  }
}
