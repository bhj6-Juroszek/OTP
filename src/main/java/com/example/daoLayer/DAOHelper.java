package com.example.daoLayer;

import com.example.daoLayer.daos.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * Created by Bartek on 2017-05-04.
 */
@Component
public class DAOHelper {

  public static final String TRAININGS_TABLE_NAME = "trainings";
  public static final String TRAININGS_INSTANCES_TABLE_NAME = "trainings_ins";
  public static final String TRAININGS_RESERVATIONS_TABLE_NAME = "trainings_rev";
  public static final String CATEGORIES_TABLE_NAME = "categories";
  public static final String RATES_TABLE_NAME = "rates";
  public static final int DEFAULT_CATEGORY_ID = 1;
  public static final String USERS_TABLE_NAME = "users";
  public static final String USERS_CATEGORIES_MAP = "categories_map";
  public static final String PROFILES_TABLE_NAME = "profiles";
  public static final String CITIES_TABLE_NAME = "cities";

  DAOHelper(@Nonnull final CategoriesDAO categoriesDAO, @Nonnull final CitiesDAO citiesDAO,
      @Nonnull final UsersDAO usersDAO, @Nonnull final ProfilesDAO profilesDAO, @Nonnull final RatesDAO ratesDAO,
      @Nonnull final TrainingsDAO trainingsDAO) {
    this.categoriesDAO = categoriesDAO;
    this.citiesDAO = citiesDAO;
    this.usersDAO = usersDAO;
    this.trainingsDAO = trainingsDAO;
    this.profilesDAO = profilesDAO;
    this.ratesDAO = ratesDAO;
    createAll();
  }

  private CategoriesDAO categoriesDAO;
  private CitiesDAO citiesDAO;
  private UsersDAO usersDAO;
  private ProfilesDAO profilesDAO;
  private RatesDAO ratesDAO;
  private TrainingsDAO trainingsDAO;

  private void createAll() {
    categoriesDAO.createTable();
    citiesDAO.createTable();
    usersDAO.createTable();
    profilesDAO.createTable();
    ratesDAO.createTable();
    trainingsDAO.createTable();
  }
}
