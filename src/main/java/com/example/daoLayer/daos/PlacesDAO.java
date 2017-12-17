package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import com.example.daoLayer.entities.Place;
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
import java.util.List;

import static com.example.daoLayer.DAOHelper.CATEGORIES_TABLE_NAME;
import static com.example.daoLayer.DAOHelper.PLACES_TABLE_NAME;

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
    if (tableExists(PLACES_TABLE_NAME)) {
      return;
    }
    template.execute
        ("CREATE TABLE " + PLACES_TABLE_NAME + " (placeId VARCHAR(50) NOT NULL, placeName VARCHAR(50), " +
            "placeLat DOUBLE, placeLng DOUBLE, " +
            "PRIMARY KEY(placeId));"
        );
  }

  public void saveToDB(@Nonnull final Place place) {
    asyncSaver.execute(() ->{
      final String SQL = "insert into " + PLACES_TABLE_NAME + " (placeId, placeName, placeLat, placeLng) values (?, " +
          "?, ?, ?)";
      template.update(SQL, place.getId(), place.getName(), place.getLat(), place.getLng());
    });
  }

  @Nullable
  public List<Place> getAll() {
    try {
      final String SQL = "select * from " + PLACES_TABLE_NAME + " ORDER BY placeName";
      final List<Place> loadedPlaces =  (ArrayList<Place>) template.query(SQL,
          new RowMapperResultSetExtractor<>(new PlaceMapper()));
      LOGGER.info("Loaded places: {}", loadedPlaces);
      return loadedPlaces;
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }
}

