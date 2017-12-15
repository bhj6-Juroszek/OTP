package com.example.daoLayer.daos;

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

  public CategoriesDAO(@Nonnull final JdbcTemplate template) {
    super(template);
  }

  @Override
  public void createTable() {
    template.execute
        ("CREATE TABLE " + CATEGORIES_TABLE_NAME + " (categoryId INT NOT NULL AUTO_INCREMENT, categoryName VARCHAR(50), categoryParent INT, " +
            "PRIMARY KEY(categoryId));"
        );
    final String SQL = "INSERT INTO " + CATEGORIES_TABLE_NAME + " (categoryName, categoryParent) VALUES (?, ?)";
    template.update(SQL, "Default", 0);
  }

  public boolean saveToDB(Category cat) {
    final String SQL = "INSERT INTO " + CATEGORIES_TABLE_NAME + " (categoryName, categoryParent) VALUES (?, ?)";
    if (!exists(cat.getName())) {

      template.update(SQL, cat.getName(), cat.getParent());

    } else {
      return false;
    }
    return true;
  }

  private boolean exists(@Nonnull final String categoryName) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + CATEGORIES_TABLE_NAME + " WHERE categoryName = ?", Integer.class, categoryName);
    return cnt != null && cnt > 0;
  }

  private boolean exists(@Nonnull final Long categoryId) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + CATEGORIES_TABLE_NAME + " WHERE categoryId = ?", Integer.class, categoryId);
    return cnt != null && cnt > 0;
  }

  public Category getCategoryById(final long categoryId) {
    try {
      String SQL = "SELECT * FROM " + CATEGORIES_TABLE_NAME + " WHERE categoryId = ?";
      return template.queryForObject(SQL,
          new Object[]{categoryId}, new CategoryMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public Category getCategoryByName(@Nonnull final String categoryName) {
    try {
      final String SQL = "SELECT * FROM " + CATEGORIES_TABLE_NAME + " WHERE categoryName = ?";
      return template.queryForObject(SQL,
          new Object[]{categoryName}, new CategoryMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public boolean updateRecord(@Nonnull final Category cat) {
    if (exists(cat.getId())) {
      final String SQL = "UPDATE " + CATEGORIES_TABLE_NAME + " SET categoryName = ?, categoryParent = ? WHERE categoryId= ?;";
      template.update(SQL, cat.getName(), cat.getParent(), cat.getId());
      return true;
    }
    return false;
  }

  @Nullable
  public List<Category> getAll() {
    try {
      final String SQL = "SELECT * FROM " + CATEGORIES_TABLE_NAME + " ORDER BY categoryName   ";
      return template.query(SQL,
          new RowMapperResultSetExtractor<>(new CategoryMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

}
