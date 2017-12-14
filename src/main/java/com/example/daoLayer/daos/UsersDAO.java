package com.example.daoLayer.daos;

import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.mappers.CategoryMapper;
import com.example.daoLayer.mappers.UserMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.daoLayer.DAOHandler.*;

@Repository
public class UsersDAO extends DAO {

  public UsersDAO(@Nonnull final JdbcTemplate template) {
    super(template);
  }

  public void createTable() {
    template.execute
        ("CREATE TABLE " + USERS_TABLE_NAME + " (userId INT NOT NULL AUTO_INCREMENT, userName VARCHAR(50)," +
            " adress VARCHAR(250), mail VARCHAR(250) UNIQUE NOT NULL, login VARCHAR(250), password VARCHAR(250), role" +
            " INT(2), " +
            "imageUrl VARCHAR(150), " +
            "confirmation VARCHAR(250), PRIMARY KEY(userId));"
        );

    template.execute(
        "CREATE TABLE " + USERS_CATEGORIES_MAP + " (mapId INT NOT NULL AUTO_INCREMENT, idUser INT, idCategory INT, " +
            "PRIMARY KEY(mapId));");
  }

  @Nullable
  public List<Category> getUserCategories(final long userId) {
    try {
      final String SQL = "select * from " + CATEGORIES_TABLE_NAME + " " +
          "INNER JOIN " + USERS_CATEGORIES_MAP + " ON userId = idCategory AND idUser = " + userId +
          " ORDER BY userName ";
      return (ArrayList<Category>) template.query(SQL,
          new RowMapperResultSetExtractor<>(new CategoryMapper()));
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public boolean saveToDB(@Nonnull final User user) {
    final String SQL = "INSERT INTO " + USERS_TABLE_NAME + " (userName, adress, mail, login, password, role," +
        "imageUrl, confirmation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    template.update(SQL, user.getName(), user.getAdress(), user.getMail(), user.getLogin(),
        user.getPassword(), user.getRole(), user.getImageUrl(), user
            .getConfirmation());
    template.update("INSERT INTO " + USERS_CATEGORIES_MAP + " (idUser, idCategory) VALUES(?,?)", user.getId(),
        DEFAULT_CATEGORY_ID);
    return true;
  }

  private boolean exists(@Nonnull final Long userId) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + USERS_TABLE_NAME + " WHERE userId = ?", Integer.class, userId);
    return cnt != null && cnt > 0;
  }

  public boolean existsAnother(@Nonnull final String userId, @Nonnull final String from, @Nonnull final Long user) {
    final Integer cnt = template.queryForObject(
        "SELECT count(*) FROM " + USERS_TABLE_NAME + " WHERE " + from + " = ? AND userId != ?", Integer.class, userId,
        user);
    return cnt != null && cnt > 0;
  }

  public void delete(@Nonnull final String value, @Nonnull final String pole) {
    final String SQL = "delete from " + USERS_TABLE_NAME + " where " + pole + " = ?";
    template.update(SQL, value);
    return;
  }

  public void delete(@Nonnull final User user) {

    final String SQL = "DELETE FROM " + USERS_TABLE_NAME + " WHERE mail = ?";
    template.update(SQL, user.getMail());
  }


  @Nullable
  public User getCustomerByMail(@Nonnull final String mail) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE mail = ?";
      return template.queryForObject(SQL,
          new Object[]{mail}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public User getCustomerByProfile(@Nonnull final Long userId) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE userId = ?";
      return template.queryForObject(SQL,
          new Object[]{userId}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public User getCustomerByConfirmation(@Nonnull final String mail) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE confirmation = ?";
      return template.queryForObject(SQL,
          new Object[]{mail}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public User getCustomerByLogin(@Nonnull final String login) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE mail = ?";
      return template.queryForObject(SQL,
          new Object[]{login}, new UserMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Nullable
  public User getCustomerById(@Nonnull final Long userId) {
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
    if (exists(user.getId())) {
      final String SQL = "UPDATE " + USERS_TABLE_NAME + " SET userName = ?, adress = ?, mail = ?, login = ?, " +
          "password = ?, role = ?, imageUrl = ?, confirmation = ?  WHERE userId= ?;";
      template.update(SQL, user.getName(), user.getAdress(), user.getMail(), user.getLogin(), user
              .getPassword(), user.getRole(), user.getImageUrl(),
          user.getConfirmation(), user
              .getId());
      return true;
    }
    return false;
  }

  @Nullable
  public ArrayList<User> getCustomersWithCategory(@Nonnull final Category category) {
    try {
      final String SQL = "SELECT * FROM " + USERS_TABLE_NAME + " " +
          "INNER JOIN USERS_CATEGORIES_MAP ON idCategory = " + category.getId() +
          " ORDER BY userName ";
      final ArrayList<User> users = (ArrayList<User>) template.query(SQL,
          new RowMapperResultSetExtractor<>(new UserMapper()));
      final Set<User> usersSet = new HashSet();
      for (User user : users) {
        usersSet.add(user);
      }
      return new ArrayList<User>(usersSet);
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

}
