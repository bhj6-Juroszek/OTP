package com.example.DAOS;

import com.example.entities.Profile;
import com.example.entities.Rate;
import com.example.mappers.ProfileMapper;
import com.example.mappers.RateMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartek on 2017-03-10.
 */
@Service
@Configurable
public class RatesDAO {

    protected RatesDAO()
    {

    }

    public static RatesDAO getInstance()
    {
        return DAOHandler.ratesDao;
    }

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Mail.xml");


    JdbcTemplate template = new JdbcTemplate((DataSource)context.getBean("customer"));

    private String ratesTableName="ratesTable";

    public boolean saveToDB(Rate rate)
    {
        String SQL="";
        SQL = "insert into "+ratesTableName+" (comment, value, profileId, fromId, date) values (?, ?, ?, ?, ?)";

        if (!exists(rate)){
            template.update(SQL, rate.getComment(), rate.getValue(),rate.getProfileId(), rate.getFromId(), rate.getDate());
        }
        else{
            return false;
        }
        return true;
    }

    public boolean exists(Rate rate)
    {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM "+ratesTableName+" WHERE profileId = ? AND fromId = ? ", Integer.class, rate.getProfileId(), rate.getFromId());
        return cnt != null && cnt > 0;
    }

    public void delete(Rate rate){

        String SQL = "delete from "+ratesTableName+" where id = ?";
        template.update(SQL, rate.getId());
    }

    public Rate getRateById(Long id) {
        try {
            String SQL = "select * from "+ratesTableName+" where id = ?";
            return template.queryForObject(SQL,
                    new Object[]{id}, new RateMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public ArrayList<Rate> getRatesByProfile(Long id) {
        try {
            String SQL = "select * from "+ratesTableName+" where profileId = ? ORDER BY date DESC  ";
            return (ArrayList<Rate>)template.query(SQL,
                    new Object[]{id}, new RowMapperResultSetExtractor<Rate>(new RateMapper()));
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public ArrayList<Rate> getRatesByFromId(Long id) {
        try {
            String SQL = "select * from "+ratesTableName+" where fromId = ? ORDER BY date DESC   ";
            return (ArrayList<Rate>)template.query(SQL,
                    new Object[]{id}, new RowMapperResultSetExtractor<Rate>(new RateMapper()));
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public Rate getProfileUserRate(Long profileId, Long userId) {
        try {
            String SQL = "select * from "+ratesTableName+" where fromId = ? AND profileId = ? ORDER BY date DESC   ";
            return template.queryForObject(SQL
                    , new Object[]{userId, profileId}
                    , new RateMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }



    public boolean updateRecord(Rate rate)
    {
        if(exists(rate)) {
            String SQL = "UPDATE " + ratesTableName + " SET comment = ?, value = ?, profileId = ?, formId = ?, date= ? WHERE id= ?;";
            template.update(SQL, rate.getComment(), rate.getValue(), rate.getProfileId(), rate.getFromId(), rate.getDate(), rate.getId());
            return true;
        }
        return false;
    }


}
