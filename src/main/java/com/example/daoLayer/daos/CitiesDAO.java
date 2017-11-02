package com.example.daoLayer.daos;

import com.example.daoLayer.entities.City;
import com.example.daoLayer.mappers.CityMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.example.daoLayer.DAOHandler.CITIES_TABLE_NAME;

/**
 * Created by Bartek on 2017-05-04.
 */
@Repository
public class CitiesDAO extends DAO {


    public CitiesDAO(@Nonnull final JdbcTemplate template)
    {
      super(template);
    }

  @Override
  public void createTable() {
    template.execute
        ("CREATE TABLE " + CITIES_TABLE_NAME + " (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(50), countryId INT, " +
            "PRIMARY KEY(id));"
        );
  }

    public boolean saveToDB(@Nonnull final City cat)
    {
        final String SQL = "insert into "+ CITIES_TABLE_NAME +" (name, country) values (?, ?)";
            template.update(SQL, cat.getName(), cat.getCountryId());
        return true;
    }

    @Nullable
    public ArrayList<City> getAll()
    {
        try {
            String SQL = "select * from "+ CITIES_TABLE_NAME +" ORDER BY name   ";
            return (ArrayList<City>)template.query(SQL,
                    new RowMapperResultSetExtractor<City>(new CityMapper()));
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    }

