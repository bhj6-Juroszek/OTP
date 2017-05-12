package com.example.DAOS;

import com.example.entities.Category;
import com.example.mappers.CategoryMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * Created by Bartek on 2017-03-11.
 */
@Service
@Configurable
public class CategoriesDAO {


    protected CategoriesDAO()
    {

    }

    public static CategoriesDAO getInstance()
    {
        return DAOHandler.catDao;
    }

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Mail.xml");


    JdbcTemplate template = new JdbcTemplate((DataSource)context.getBean("customer"));

    private String categoriesTableName="categoriesTable";
    public boolean saveToDB(Category cat)
    {
        String SQL="";
        SQL = "insert into "+categoriesTableName+" (name, parent) values (?, ?)";

        if (!exists(cat.getName())) {

                template.update(SQL, cat.getName(), cat.getParent());

        }
        else{
            return false;
        }
        return true;
    }


    public boolean exists(String name)
    {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM "+categoriesTableName+" WHERE name = ?", Integer.class, name);
        return cnt != null && cnt > 0;
    }

    public boolean exists(Long id)
    {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM "+categoriesTableName+" WHERE id = ?", Integer.class, id);
        return cnt != null && cnt > 0;
    }

    public Category getCategoryById(long id)
    {
        try {
            String SQL = "select * from "+categoriesTableName+" where id = ?";
            return template.queryForObject(SQL,
                    new Object[]{id}, new CategoryMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public Category getCategoryByName(String name)
    {
        try {
            String SQL = "select * from "+categoriesTableName+" where name = ?";
            return template.queryForObject(SQL,
                    new Object[]{name}, new CategoryMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public boolean updateRecord(Category cat)
    {
        if(exists(cat.getId())) {
            String SQL = "UPDATE " + categoriesTableName + " SET name = ?, parent = ? WHERE id= ?;";
            template.update(SQL, cat.getName(), cat.getParent(), cat.getId());
            return true;
        }
        return false;
    }

    public ArrayList<Category> getAll()
    {
        try {
            String SQL = "select * from "+categoriesTableName+" ORDER BY name   ";
            return (ArrayList<Category>)template.query(SQL,
                    new RowMapperResultSetExtractor<Category>(new CategoryMapper()));
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

}
