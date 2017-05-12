package com.example.DAOS;

import com.example.entities.Category;
import com.example.entities.City;
import com.example.mappers.CityMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartek on 2017-05-04.
 */
public class CitiesDAO {

    protected CitiesDAO()
    {

    }

    public static CitiesDAO getInstance()
    {
        return DAOHandler.citDAO;
    }

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Mail.xml");


    JdbcTemplate template = new JdbcTemplate((DataSource)context.getBean("customer"));

    private String citiesTableName="Cities";
    public boolean saveToDB(City cat)
    {
        String SQL="";
        SQL = "insert into "+citiesTableName+" (name, country) values (?, ?)";

            template.update(SQL, cat.getName(), cat.getCountryId());

        return true;
    }

    public ArrayList<City> getAll()
    {
        try {
            String SQL = "select * from "+citiesTableName+" ORDER BY name   ";
            return (ArrayList<City>)template.query(SQL,
                    new RowMapperResultSetExtractor<City>(new CityMapper()));
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    }

