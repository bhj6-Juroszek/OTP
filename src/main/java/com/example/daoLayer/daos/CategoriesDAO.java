package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.mappers.CategoryMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.example.daoLayer.DAOHelper.CATEGORIES_TABLE_NAME;

/**
 * Created by Bartek on 2017-03-11.
 */
@Repository
public class CategoriesDAO extends DAO {

  CategoriesDAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    super(template, asyncDbSaver);
  }

  @Override
  public void createTable() {
    if (tableExists(CATEGORIES_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + CATEGORIES_TABLE_NAME + " (categoryId VARCHAR(50) NOT NULL, categoryName VARCHAR(50), " +
            "categoryParent VARCHAR(50), theoretical INT(2), " +
            "PRIMARY KEY(categoryId));"
        );
  }

  public Category getCategoryById(final String categoryId) {
    try {
      String SQL = "SELECT * FROM " + CATEGORIES_TABLE_NAME + " WHERE categoryId = ?";
      return template.queryForObject(SQL,
          new Object[]{categoryId}, new CategoryMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public List<Category> getAll() {
    try {
      final String SQL = "SELECT * FROM " + CATEGORIES_TABLE_NAME + " ORDER BY categoryName";
      return template.query(SQL,
          new RowMapperResultSetExtractor<>(new CategoryMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

}
