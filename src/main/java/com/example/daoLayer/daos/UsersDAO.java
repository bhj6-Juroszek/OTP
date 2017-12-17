package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.mappers.UserMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Types;
import java.util.List;

import static com.example.daoLayer.DAOHelper.USERS_TABLE_NAME;

@Repository
public class UsersDAO extends DAO {

  UsersDAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    super(template, asyncDbSaver);
  }

  public void createTable() {
    if (tableExists(USERS_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + USERS_TABLE_NAME + " (userId VARCHAR (50) NOT NULL, userName VARCHAR(50)," +
            " adress VARCHAR(250), mail VARCHAR(250) UNIQUE NOT NULL, login VARCHAR(250), password VARCHAR(250), role" +
            " INT(2), " +
            "imageUrl VARCHAR(150), " +
            "confirmation VARCHAR(250), PRIMARY KEY(userId));"
        );
  }

  public boolean saveToDB(@Nonnull final User user) {
    final String SQL = "INSERT INTO " + USERS_TABLE_NAME + " (userId, userName, adress, mail, login, password, role," +
        "imageUrl, confirmation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    template.update(SQL, user.getId(), user.getName(), user.getAdress(), user.getMail(), user.getLogin(),
        user.getPassword(), user.getRole(), user.getImageUrl(), user
            .getConfirmation());
    return true;
  }

  public boolean existsAnother(@Nonnull final String value, @Nonnull final String column,
      @Nonnull final String userId) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + USERS_TABLE_NAME + " WHERE " + column + " = ? AND userId != ?", Integer.class, value,
        userId);
    return cnt != null && cnt > 0;
  }

  @Nullable
  public User getUserByMail(@Nonnull final String mail) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE mail = ?";
      return template.queryForObject(SQL,
          new Object[]{mail}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public User getUserByConfirmation(@Nonnull final String confirmation) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE confirmation = ?";
      return template.queryForObject(SQL,
          new Object[]{confirmation}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public User getUserByLogin(@Nonnull final String login) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE mail = :login OR login = :login ";
      final List<User> users = parameterJdbcTemplate
          .query(SQL, new MapSqlParameterSource().addValue("login", login, Types.VARCHAR),
              new UserMapper());
      if (users.size() == 1) {
        return users.get(0);
      }
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
    return null;
  }

  @Nullable
  public User getUserById(@Nonnull final String userId) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE userId = ?";
      return template.queryForObject(SQL,
          new Object[]{userId}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {

      return null;
    }
  }

  public int getSize() {
    return template.queryForList("SELECT * FROM " + USERS_TABLE_NAME).size();
  }

  public void updatePassword(@Nonnull final String uuid, @Nonnull final String password) {
    asyncSaver.execute(() -> {
      final String SQL = "UPDATE " + USERS_TABLE_NAME + " SET password = ? WHERE userId = ?;";
      template.update(SQL, password, uuid);
    });
  }

  public void updateRecord(@Nonnull final User user) {
    asyncSaver.execute(() -> {
      final String SQL = "UPDATE " + USERS_TABLE_NAME + " SET userName = ?, adress = ?, mail = ?, login = ?, imageUrl" +
          " = ? WHERE userId = ?;";
      template.update(SQL, user.getName(), user.getAdress(), user.getMail(), user.getLogin(), user.getImageUrl(), user
          .getId());
    });
  }

}
