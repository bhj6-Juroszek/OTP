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
        Category cat = new Category();
        cat.setId(rs.getInt("categoryId"));
        cat.setName(rs.getString("categoryName"));
        cat.setParent(rs.getInt("categoryParent"));

        return cat;

    }
}
