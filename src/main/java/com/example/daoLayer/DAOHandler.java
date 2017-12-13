package com.example.daoLayer;

import com.example.daoLayer.daos.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by Bartek on 2017-05-04.
 */
public class DAOHandler {

  public static final String TRAININGS_TABLE_NAME = "trainings";
  public static final String CATEGORIES_TABLE_NAME = "categories";
  public static final String RATES_TABLE_NAME = "rates";
  public static final int DEFAULT_CATEGORY_ID = 1;
  public static final String USERS_TABLE_NAME = "users";
  public static final String USERS_CATEGORIES_MAP = "categories_map";
  public static final String PROFILES_TABLE_NAME = "profiles";
  public static final String CITIES_TABLE_NAME = "cities";

  private static final ApplicationContext context =
      new ClassPathXmlApplicationContext("Mail.xml");
  private static final JdbcTemplate template = new JdbcTemplate((DataSource) context.getBean("dbSource"));

  public static CategoriesDAO categoriesDAO = new CategoriesDAO(template);
  public static CitiesDAO citiesDAO = new CitiesDAO(template);
  public static UsersDAO usersDAO = new UsersDAO(template);
  public static ProfilesDAO profilesDAO = new ProfilesDAO(template);
  public static RatesDAO ratesDAO = new RatesDAO(template);
  public static TrainingsDAO trainingsDAO = new TrainingsDAO(template);

  public static void createAll() {
    categoriesDAO.createTable();
    citiesDAO.createTable();
    usersDAO.createTable();
    profilesDAO.createTable();
    ratesDAO.createTable();
    trainingsDAO.createTable();
  }
}