package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.mappers.UserMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.daoLayer.DAOHelper.*;

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
        ("CREATE TABLE " + USERS_TABLE_NAME + " (userId INT NOT NULL AUTO_INCREMENT, userName VARCHAR(50)," +
            " adress VARCHAR(250), mail VARCHAR(250) UNIQUE NOT NULL, login VARCHAR(250), password VARCHAR(250), role" +
            " INT(2), " +
            "imageUrl VARCHAR(150), " +
            "confirmation VARCHAR(250), PRIMARY KEY(userId));"
        );
  }

  public boolean saveToDB(@Nonnull final User user) {
    final String SQL = "INSERT INTO " + USERS_TABLE_NAME + " (userName, adress, mail, login, password, role," +
        "imageUrl, confirmation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    template.update(SQL, user.getName(), user.getAdress(), user.getMail(), user.getLogin(),
        user.getPassword(), user.getRole(), user.getImageUrl(), user
            .getConfirmation());
    return true;
  }

  public boolean existsAnother(@Nonnull final String userId, @Nonnull final String from, @Nonnull final String user) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + USERS_TABLE_NAME + " WHERE " + from + " = ? AND userId != ?", Integer.class, userId,
        user);
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
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE mail = ?";
      return template.queryForObject(SQL,
          new Object[]{login}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
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

  public boolean updateRecord(@Nonnull final User user) {
    final String SQL = "UPDATE " + USERS_TABLE_NAME + " SET userName = ?, adress = ?, mail = ?, login = ?, " +
        "password = ?, role = ?, imageUrl = ?, confirmation = ?  WHERE userId = ?;";
    template.update(SQL, user.getName(), user.getAdress(), user.getMail(), user.getLogin(), user
            .getPassword(), user.getRole(), user.getImageUrl(),
        user.getConfirmation(), user
            .getId());
    return true;
  }

}
