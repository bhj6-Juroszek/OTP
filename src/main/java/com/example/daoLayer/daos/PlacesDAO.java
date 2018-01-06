package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Country;
import com.example.daoLayer.entities.Place;
import com.example.daoLayer.mappers.CountryMapper;
import com.example.daoLayer.mappers.PlaceMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static com.example.daoLayer.DAOHelper.COUNTRIES_TABLE_NAME;
import static com.example.daoLayer.DAOHelper.PLACES_TABLE_NAME;
import static java.util.Collections.emptyList;

/**
 * Created by Bartek on 2017-05-04.
 */
@Repository
public class PlacesDAO extends DAO {

  private static final Logger LOGGER = LogManager.getLogger(PlacesDAO.class);

  PlacesDAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    super(template, asyncDbSaver);
  }

  @Override
  public void createTable() {
    if (!tableExists(PLACES_TABLE_NAME)) {
      template.execute
          ("CREATE TABLE " + PLACES_TABLE_NAME + " (placeId VARCHAR(50) NOT NULL, placeName VARCHAR(250), " +
              "placeLat DOUBLE, placeLng DOUBLE, countryCode VARCHAR(10), " +
              "PRIMARY KEY(placeId));"
          );
    }
    if (tableExists(COUNTRIES_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + COUNTRIES_TABLE_NAME + " (countryId VARCHAR(10) NOT NULL, countryName VARCHAR(250), " +
            "PRIMARY KEY(countryId));"
        );
  }

  public void saveToDB(@Nonnull final Place place) {
    asyncSaver.execute(() -> {
      try {
        final String SQL = "insert into " + PLACES_TABLE_NAME + " (placeId, placeName, placeLat, placeLng, " +
            "countryCode) values (?, " +
            "?, ?, ?, ?)";
        template.update(SQL, place.getId(), place.getName(), place.getLat(), place.getLng(), place.getCountryCode());
      } catch (Exception e) {
        LOGGER.error(e);
      }
    });
  }

  public void saveToDB(@Nonnull final Country country) {
    asyncSaver.execute(() -> {
      try {
        final String SQL = "insert into " + COUNTRIES_TABLE_NAME + " (countryId, countryName) values (?, ?)";
        template.update(SQL, country.getCountryId(), country.getCountryName());
      } catch (Exception e) {
        LOGGER.error(e);
      }
    });
  }

  private List<Place> getCities(@Nonnull final String countryCode) {
    try {
      final String SQL = "select * from " + PLACES_TABLE_NAME + " WHERE countryCode = ?";
      final List<Place> loadedPlaces = template.query(SQL, new Object[]{countryCode},
          new RowMapperResultSetExtractor<>(new PlaceMapper()));
      LOGGER.info("Loaded places: {}", loadedPlaces);
      final Set<Place> placesSet = new HashSet<>(loadedPlaces);
     return new ArrayList<>(placesSet);
//      return loadedPlaces;
    } catch (EmptyResultDataAccessException ex) {
      return emptyList();
    }
  }

  public List<Place> getCities(@Nonnull final String countryCode, @Nullable final String prefix) {
    if (prefix == null) {
      return getCities(countryCode);
    }
    try {
      final String SQL = "SELECT * FROM " + PLACES_TABLE_NAME + " where countryCode = ? AND lower(placeName) LIKE " +
          "lower(?);";
      final List<Place> loadedPlaces = template.query(SQL,
          new Object[]{countryCode, (prefix+"%")},
          new RowMapperResultSetExtractor<>(new PlaceMapper()));
      LOGGER.info("Loaded places: {}", loadedPlaces);
      final Set<Place> placesSet = new HashSet<>(loadedPlaces);
      return new ArrayList<>(placesSet);
//      return loadedPlaces;
    } catch (EmptyResultDataAccessException ex) {
      return emptyList();
    }
  }

  public List<Country> getCountries() {
    try {
      final String SQL = "select * from " + COUNTRIES_TABLE_NAME + " ORDER BY countryId";
      final List<Country> countries = template.query(SQL,
          new RowMapperResultSetExtractor<>(new CountryMapper()));
      LOGGER.info("Loaded countries: {}", countries);
      return countries;
    } catch (EmptyResultDataAccessException ex) {
      return emptyList();
    }
  }
}

