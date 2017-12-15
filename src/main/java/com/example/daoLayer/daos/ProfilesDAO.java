package com.example.daoLayer.daos;

import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.mappers.ProfileMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.example.daoLayer.DAOHelper.PROFILES_TABLE_NAME;

/**
 * Created by Bartek on 2017-03-10.
 */
@Repository
public class ProfilesDAO extends DAO {

  public ProfilesDAO(@Nonnull final JdbcTemplate template) {
    super(template);
  }

  @Override
  public void createTable() {
    template.execute
        ("CREATE TABLE " + PROFILES_TABLE_NAME + " (id INT NOT NULL AUTO_INCREMENT, userId INT, text VARCHAR(1500), " +
            "PRIMARY KEY(id));"
        );
  }

  public boolean saveToDB(@Nonnull final Profile profile) {
    final String SQL = "INSERT INTO " + PROFILES_TABLE_NAME + " (userId, text) VALUES (?, ?)";

    if (!exists(profile)) {
      template.update(SQL, profile.getUserId(), profile.getText());
    } else {
      return false;
    }
    return true;
  }

  private boolean exists(@Nonnull final Profile profile) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + PROFILES_TABLE_NAME + " WHERE userId = ?", Integer.class, profile.getUserId());
    return cnt != null && cnt > 0;
  }

  public void delete(@Nonnull final Profile profile) {

    final String SQL = "DELETE FROM " + PROFILES_TABLE_NAME + " WHERE id = ?";
    template.update(SQL, profile.getId());
  }

  @Nullable
  public Profile getProfileById(@Nonnull final Long id) {
    try {
      final String SQL = "SELECT * FROM " + PROFILES_TABLE_NAME + " WHERE id = ?";
      return template.queryForObject(SQL,
          new Object[]{id}, new ProfileMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public Profile getProfileByUser(@Nonnull final Long id) {
    try {
      final String SQL = "SELECT * FROM " + PROFILES_TABLE_NAME + " WHERE userId = ?";
      return template.queryForObject(SQL,
          new Object[]{id}, new ProfileMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public boolean updateRecord(@Nonnull final Profile profile) {
    if (exists(profile)) {
      final String SQL = "UPDATE " + PROFILES_TABLE_NAME + " SET userId = ?, text = ? WHERE id= ?;";
      template.update(SQL, profile.getUserId(), profile.getText(), profile.getId());
      return true;
    }
    return false;
  }
}
