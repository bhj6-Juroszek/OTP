package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.mappers.ProfileMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.example.daoLayer.DAOHelper.PLACES_TABLE_NAME;
import static com.example.daoLayer.DAOHelper.PROFILES_TABLE_NAME;

/**
 * Created by Bartek on 2017-03-10.
 */
@Repository
public class ProfilesDAO extends DAO {

  ProfilesDAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    super(template, asyncDbSaver);
  }

  @Override
  public void createTable() {
    if (tableExists(PROFILES_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + PROFILES_TABLE_NAME + " (profileId VARCHAR(50) NOT NULL, profileOwnerId VARCHAR(50), profileContent VARCHAR(1500), " +
            "PRIMARY KEY(profileId));"
        );
  }

  public boolean saveToDB(@Nonnull final Profile profile) {
    final String SQL = "INSERT INTO " + PROFILES_TABLE_NAME + " (profileId, profileOwnerId, profileContent) VALUES (?, ?)";

    if (!exists(profile)) {
      template.update(SQL, profile.getId(), profile.getOwnerId(), profile.getContent());
    } else {
      return false;
    }
    return true;
  }

  private boolean exists(@Nonnull final Profile profile) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + PROFILES_TABLE_NAME + " WHERE profileOwnerId = ?", Integer.class, profile.getOwnerId());
    return cnt != null && cnt > 0;
  }

  public void delete(@Nonnull final Profile profile) {

    final String SQL = "DELETE FROM " + PROFILES_TABLE_NAME + " WHERE profileId = ?";
    template.update(SQL, profile.getId());
  }

  @Nullable
  public Profile getProfileById(@Nonnull final String profileId) {
    try {
      final String SQL = "SELECT * FROM " + PROFILES_TABLE_NAME + " WHERE profileId = ?";
      return template.queryForObject(SQL,
          new Object[]{profileId}, new ProfileMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public Profile getProfileByUser(@Nonnull final String profileId) {
    try {
      final String SQL = "SELECT * FROM " + PROFILES_TABLE_NAME + " WHERE profileOwnerId = ?";
      return template.queryForObject(SQL,
          new Object[]{profileId}, new ProfileMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public boolean updateRecord(@Nonnull final Profile profile) {
    if (exists(profile)) {
      final String SQL = "UPDATE " + PROFILES_TABLE_NAME + " SET profileOwnerId = ?, profileContent = ? WHERE profileId= ?;";
      template.update(SQL, profile.getOwnerId(), profile.getContent(), profile.getId());
      return true;
    }
    return false;
  }
}
