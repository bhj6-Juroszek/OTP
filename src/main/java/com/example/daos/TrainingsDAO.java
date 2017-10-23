package com.example.daos;

import com.example.model.JsonReader;
import com.example.model.Place;
import com.example.entities.Category;
import com.example.entities.Training;
import com.example.mappers.TrainingMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Bartek on 2017-03-25.
 */
@Service
@Configurable
public class TrainingsDAO {


    protected TrainingsDAO()
    {

    }

    public static TrainingsDAO getInstance()
    {
        return DAOHandler.trDao;
    }
    ApplicationContext context =
            new ClassPathXmlApplicationContext("Mail.xml");


    JdbcTemplate template = new JdbcTemplate((DataSource) context.getBean("customer"));

    private String trainingsTableName = "trainingsTable";

    public boolean saveToDB(Training training) {
        String SQL = "";
        SQL = "insert into " + trainingsTableName + " (date, hour, length, price, category, city, description, profileId, takenById) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (!exists(training)) {
            template.update(SQL, training.getDate(), training.getHour(),training.getLength(), training.getPrice(), training.getCategory(),training.getCity(), training.getDescription(), training.getProfileId(), training.getTakenById());
        } else {
            return false;
        }
        return true;
    }

    public boolean updateRecord(Training training) {
        if (existsById(training)) {
            String SQL = "UPDATE " + trainingsTableName + " SET date = ?, hour = ?, length = ?, price = ?, category = ?, city = ?,description = ?,  profileId= ?, takenById= ?  WHERE id= ?;";
            template.update(SQL, training.getDate(), training.getHour(), training.getLength(), training.getPrice(), training.getCategory(), training.getCity(), training.getDescription(), training.getProfileId(), training.getTakenById(), training.getId());
            return true;
        }
        return false;

    }

    public boolean existsById(Training training) {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM " + trainingsTableName + " WHERE id = ?  ", Integer.class, training.getId());
        return cnt != null && cnt > 0;
    }

    public boolean exists(Training training) {

        for (Training tr : this.getTrainingsFromDate(training.getProfileId(), training.getDate())) {
            if (tr.getHour() > training.getHour() && tr.getHour() < training.getHour() + training.getLength())
                return true;
            if (tr.getHour() + tr.getLength() > training.getHour() && tr.getHour() + tr.getLength() < training.getHour() + training.getLength())
                return true;
            if (tr.getHour() == training.getHour() && tr.getLength() == training.getLength())
                return true;
        }
        return false;
    }


    public void delete(Training training) {

        String SQL = "delete from " + trainingsTableName + " where id = ? ";
        template.update(SQL, training.getId());
    }
    public void clear() {
        java.util.Date date=new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String SQL = "delete from " + trainingsTableName + " where date < ?";
        template.update(SQL,sqlDate);
    }

    public Training getTrainingById(Long id) {
        try {
            String SQL = "select * from " + trainingsTableName + " where id = ?";
            return template.queryForObject(SQL,
                    new Object[] {id}, new TrainingMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public ArrayList<Training> getTrainingsFromDate(Long fromId, Date date) {
        try {
            String SQL = "select * from " + trainingsTableName + " where profileId = ? AND date = ? ";
            return (ArrayList<Training>) template.query(SQL,
                    new Object[] {fromId,date}, new RowMapperResultSetExtractor<Training>(new TrainingMapper()));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
    public ArrayList<Training> getTrainingsFromDate(Date date) {
        try {
            String SQL = "select * from " + trainingsTableName + " where date = ? ";
            return (ArrayList<Training>) template.query(SQL,
                    new Object[] {date}, new RowMapperResultSetExtractor<Training>(new TrainingMapper()));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public ArrayList<Training> getTrainingsByFilter(String cityName, double range, Category cat, Date dateFirst, Date dateLast, double maxPrice, String sortBy, boolean showOnline)
    {
        ArrayList<Training> result=new ArrayList<>();
        try {
            Object[] list = null;
            String SQL = "";
            if (dateFirst == null && maxPrice == 0) {
                SQL = "select * from " + trainingsTableName + "where category = ? AND takenById = 0 ORDER BY ";
                list = new Object[] {cat.getId()};
            } else if (dateFirst == null && maxPrice != 0) {
                SQL = "select * from " + trainingsTableName + " where category = ? AND takenById = 0 AND price < ?  ORDER BY ";
                list = new Object[] {cat.getId(), maxPrice};
            } else if (dateFirst != null && maxPrice == 0) {
                SQL = "select * from " + trainingsTableName + " where category = ? AND takenById = 0 AND date BETWEEN ? AND ?  ORDER BY ";
                list = new Object[] {cat.getId(), dateFirst, dateLast};
            } else if (dateFirst != null && maxPrice != 0) {
                SQL = "select * from " + trainingsTableName + " where category = ? AND takenById = 0 AND date BETWEEN ? AND ? AND price < ? ORDER BY ";
                list = new Object[] {cat.getId(), dateFirst, dateLast, maxPrice};
            }

            if (sortBy != null) {
                SQL += sortBy;
            } else {
                SQL += "DATE";
            }
            JsonReader reader = new JsonReader();
            ArrayList<Training> trainings=(ArrayList<Training>) template.query(SQL,
                    list, new RowMapperResultSetExtractor<Training>(new TrainingMapper()));
            if (range != 0 && cityName!=null)
            {
                Place place1=null;
                Place place2 = null;
                    place2 = reader.getCity(cityName);

                for (int i=0;i<trainings.size();i++ ) {
                        if(trainings.get(i).getCity().equals(""))
                        {
                            if(showOnline)
                            {
                                result.add(trainings.get(i));
                            }
                        }
                        if(i>0 && !trainings.get(i).getCity().equals(trainings.get(i-1).getCity())) {
                            place1 = reader.getCity(trainings.get(i).getCity());
                        }
                        if (place1 != null) {
                            if (reader.distance(place1, place2) < range) {
                                result.add(trainings.get(i));
                            }
                        }
                }
        }
        else{
                for (int i=0;i<trainings.size();i++ ) {
                    if(trainings.get(i).getCity().equals(""))
                    {
                        if(showOnline)
                        {
                            result.add(trainings.get(i));
                        }
                    }
                else{
                        result.add(trainings.get(i));
                    }
                }
            }
        } catch (EmptyResultDataAccessException ex) {
        }

        return result;
    }
}





