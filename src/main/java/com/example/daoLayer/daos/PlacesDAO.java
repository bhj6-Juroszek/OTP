package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Country;
import com.example.daoLayer.entities.Place;
import com.example.daoLayer.mappers.CityMapper;
import com.example.daoLayer.mappers.CountryMapper;
import com.example.daoLayer.mappers.PlaceMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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

  private String resolveTableName(@Nonnull final String countryCode) {
    if(countryCode.contains(" ")) return "unknown";
    return "cities_of_"+countryCode;
  }

  private void createTable(@Nonnull final String tableName) {

  }

  @Override
  public void createTable() {
    List<Country> countries = getCountries();
    CompletableFuture<Void> updatedCountry = new CompletableFuture<>();
    updatedCountry.complete(null);
    for(Country country: countries) {
      try {
        String SQL = "SELECT * FROM " + PLACES_TABLE_NAME + " WHERE countryCode = \""+country.getCountryId()+"\"";
        final List<Place> loadedPlaces = template.query(SQL, new RowMapperResultSetExtractor<>(new PlaceMapper()));
        String tableName;
        if (!tableExists((tableName = resolveTableName(country.getCountryId())))) {
          template.execute
              ("CREATE TABLE " + tableName + " (placeName VARCHAR(250), placeLat DOUBLE, placeLng DOUBLE, " +
                  "PRIMARY KEY(placeName, placeLat, placeLng));"
              );
        }
        updatedCountry.get();
        updatedCountry = new CompletableFuture<>();
        for (Place loadedPlace : loadedPlaces) {
          asyncSaver.execute(() -> {
            String SQL2 = "DELETE FROM " + PLACES_TABLE_NAME + " WHERE placeId = ?";
            template.update(SQL2, loadedPlace.getId());
            LOGGER.info("deleted");
            SQL2 = "insert into " + tableName + " (placeName, placeLat, placeLng) values (?, ?, ?)";
            template.update(SQL2, loadedPlace.getName(), loadedPlace.getLat(), loadedPlace.getLng());

            LOGGER.info("inserted");
          });
        }
        updatedCountry.complete(null);
        } catch(Exception e){
        if (e instanceof CannotGetJdbcConnectionException) {
          try {
            template.getDataSource().getConnection().close();
            createTable();
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        }
          LOGGER.error(e);
        }

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
        final String SQL = "insert into " + resolveTableName(place.getCountryCode()) + " (placeName, placeLat, placeLng, " +
            "countryCode) values (?, " +
            "?, ?, ?)";
        template.update(SQL, place.getName(), place.getLat(), place.getLng(), place.getCountryCode());
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
      final String SQL = "select * from " + resolveTableName(countryCode);
      final List<Place> loadedPlaces = template.query(SQL, new RowMapperResultSetExtractor<>(new CityMapper()));
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
      final String SQL = "SELECT * FROM " + resolveTableName(countryCode) + " where lower(placeName) LIKE " +
          "lower(?);";
      final List<Place> loadedPlaces = template.query(SQL,
          new Object[]{(prefix+"%")},
          new RowMapperResultSetExtractor<>(new CityMapper()));
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

