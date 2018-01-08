package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.mappers.extractors.ProfilesExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.example.daoLayer.DAOHelper.PROFILES_TABLE_NAME;
import static java.sql.Types.VARCHAR;

/**
 * Created by Bartek on 2017-03-10.
 */
@Repository
public class ProfilesDAO extends DAO {

  private static final Logger LOGGER = LogManager.getLogger(ProfilesDAO.class);

  ProfilesDAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    super(template, asyncDbSaver);
  }

  @Override
  public void createTable() {
    //Todo update
//    if (tableExists(PROFILES_TABLE_NAME)) {
//      return;
//    }
//    template.execute
//        ("CREATE TABLE " + PROFILES_TABLE_NAME + " (profileId VARCHAR(50) NOT NULL, profileOwnerId VARCHAR(50), " +
//            "profileContent VARCHAR(1500), " +
//            "PRIMARY KEY(profileId));"
//        );
  }

  public void saveToDB(@Nonnull final Profile profile) {
    asyncSaver.execute(() -> {
      try {
        final String SQL = "INSERT INTO " + PROFILES_TABLE_NAME + " (profileId, profileOwnerId, profileContent, " +
            "showMail," +
            " showAddress, facebookLink, linkedInLink, phoneNumber ) VALUES (?,?,?,?,?,?,?,?)";
        template.update(SQL, profile.getId(), profile.getOwnerId(), profile.getContent(), profile.isShowMail(),
            profile.isShowAddress(), profile.getFacebookLink(), profile.getLinkedInLink(), profile.getPhoneNumber());
      } catch (Exception e) {
        LOGGER.error(e);
      }
    });
  }


  @Nullable
  public Profile getProfileByUser(@Nonnull final String ownerId) {
    try {
      final String SQL = "SELECT * FROM " + PROFILES_TABLE_NAME + " " +
          "LEFT OUTER JOIN social_links ON userId = :ownerId " +
          "LEFT OUTER JOIN rates ON rateduserId = :ownerId " +
          "WHERE profileOwnerId = :ownerId";
      final List<Profile> profiles = parameterJdbcTemplate
          .query(SQL, new MapSqlParameterSource().addValue("ownerId", ownerId, VARCHAR),
              new ProfilesExtractor());
      if (profiles.isEmpty() || profiles.size() > 1) {
        return null;
      }
      return profiles.get(0);
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public void updateProfile(@Nonnull final Profile profile) {
    asyncSaver.execute(() -> {
      try {
        String SQL = "UPDATE " + PROFILES_TABLE_NAME + " SET profileContent = ?, showMail = ?," +
            "showAddress = ?, facebookLink = ?, linkedInLink = ?, phoneNumber = ?  WHERE " +
            "profileId= ?";
        template
            .update(SQL, profile.getContent(), profile.isShowMail(), profile.isShowAddress(), profile.getFacebookLink(),
                profile.getLinkedInLink(), profile.getPhoneNumber(), profile.getId());
        SQL = "DELETE  FROM social_links  WHERE userId = ?";
        template.update(SQL, profile.getOwnerId());
        for (String socialLink : profile.getSocialMediaLinks()) {
          SQL = "INSERT INTO  social_links (userId, value) VALUES (?,?)";
          template.update(SQL, profile.getOwnerId(), socialLink);
        }
      } catch (Exception ex) {
        LOGGER.error(ex);
      }
    });

  }
}
