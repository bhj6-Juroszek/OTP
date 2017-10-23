package com.example.daos;

import com.example.entities.Profile;
import com.example.mappers.ProfileMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by Bartek on 2017-03-10.
 */
@Service
@Configurable
public class ProfilesDAO {

    protected ProfilesDAO()
    {

    }

    public static ProfilesDAO getInstance()
    {
        return DAOHandler.profDao;
    }
    ApplicationContext context =
            new ClassPathXmlApplicationContext("Mail.xml");


    JdbcTemplate template = new JdbcTemplate((DataSource)context.getBean("customer"));

    private String profilesTableName="profilesTable";

    public boolean saveToDB(Profile profile)
    {
        String SQL="";
        SQL = "insert into "+profilesTableName+" (userId, text) values (?, ?)";

            if (!exists(profile)){
                template.update(SQL, profile.getUserId(), profile.getText());
        }
        else{
            return false;
        }
        return true;
    }

    public boolean exists(Profile profile)
    {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM "+profilesTableName+" WHERE userId = ?", Integer.class, profile.getUserId());
        return cnt != null && cnt > 0;
    }

    public void delete(Profile profile){

        String SQL = "delete from "+profilesTableName+" where id = ?";
        template.update(SQL, profile.getId());
    }

    public Profile getProfileById(Long id) {
        try {
            String SQL = "select * from "+profilesTableName+" where id = ?";
            return template.queryForObject(SQL,
                    new Object[]{id}, new ProfileMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public Profile getProfileByUser(Long id) {
        try {
            String SQL = "select * from "+profilesTableName+" where userId = ?";
            return template.queryForObject(SQL,
                    new Object[]{id}, new ProfileMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public boolean updateRecord(Profile profile)
    {
        if(exists(profile)) {
            String SQL = "UPDATE " + profilesTableName + " SET userId = ?, text = ? WHERE id= ?;";
            template.update(SQL, profile.getUserId(), profile.getText(), profile.getId());
            return true;
        }
        return false;
    }
}
