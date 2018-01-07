package com.example.daoLayer;

import com.example.daoLayer.daos.*;
import com.example.utils.PlacesLoader;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

/**
 * Created by Bartek on 2017-05-04.
 */
@Component
@Order(9)
public class DAOHelper {

  public static final String TRAININGS_TABLE_NAME = "trainings";
  public static final String TRAININGS_INSTANCES_TABLE_NAME = "trainings_ins";
  public static final String TRAININGS_RESERVATIONS_TABLE_NAME = "trainings_rev";
  public static final String CATEGORIES_TABLE_NAME = "categories";
  public static final String RATES_TABLE_NAME = "rates";
  public static final int DEFAULT_CATEGORY_ID = 1;
  public static final String USERS_TABLE_NAME = "users";
  public static final String PROFILES_TABLE_NAME = "profiles";
  public static final String PLACES_TABLE_NAME = "cities";
  public static final String COUNTRIES_TABLE_NAME = "countries";

  DAOHelper(@Nonnull final CategoriesDAO categoriesDAO, @Nonnull final PlacesDAO placesDAO,
      @Nonnull final UsersDAO usersDAO, @Nonnull final ProfilesDAO profilesDAO, @Nonnull final RatesDAO ratesDAO,
      @Nonnull final TrainingsDAO trainingsDAO, @Nonnull final PlacesLoader placesLoader) {
    this.categoriesDAO = categoriesDAO;
    this.placesDAO = placesDAO;
    this.usersDAO = usersDAO;
    this.trainingsDAO = trainingsDAO;
    this.profilesDAO = profilesDAO;
    this.ratesDAO = ratesDAO;
    this.loader = placesLoader;
  }

  private CategoriesDAO categoriesDAO;
  private PlacesDAO placesDAO;
  private UsersDAO usersDAO;
  private ProfilesDAO profilesDAO;
  private RatesDAO ratesDAO;
  private TrainingsDAO trainingsDAO;
  private PlacesLoader loader;


  @PostConstruct
  private void createAll() {
    categoriesDAO.createTable();
    placesDAO.createTable();
    usersDAO.createTable();
    profilesDAO.createTable();
    ratesDAO.createTable();
    trainingsDAO.createTable();
  }

  /**
   * Method used to load Cities and countries from CSV files
   */

//  @PostConstruct
//  private void loadCountries() {
////    loader.saveCities(placesDAO);
////    loader.saveCountries(placesDAO);
//  }
}
