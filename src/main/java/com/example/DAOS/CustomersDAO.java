package com.example.DAOS;

import com.example.entities.Category;
import com.example.entities.Customer;
import com.example.entities.Rate;
import com.example.mappers.CustomerMapper;
import com.example.mappers.RateMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;

@Service
@Configurable
public class CustomersDAO {

    protected CustomersDAO()
    {

    }

    public static CustomersDAO getInstance()
    {
        return DAOHandler.custDao;
    }
    ApplicationContext context =
            new ClassPathXmlApplicationContext("Mail.xml");


    JdbcTemplate template = new JdbcTemplate((DataSource)context.getBean("customer"));

    private String customersTableName="userTable";
    public void createTable()
    {
        template.execute
                ("CREATE TABLE "+customersTableName+" (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(50), surname VARCHAR(50), adress VARCHAR(250), mail VARCHAR(250), login VARCHAR(250), password VARCHAR(250), role VARCHAR(50),confirmation VARCHAR(250), PRIMARY KEY(id));"
                );
    }

    public boolean saveToDB(Customer customer)
    {
        String SQL="";
            SQL = "insert into "+customersTableName+" (name, surname, adress, mail, login, password, role, categories,imageUrl, profileId, confirmation) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                   template.update(SQL, customer.getName(), customer.getSurname(), customer.getAdress(), customer.getMail(), customer.getLogin(),customer.getPassword(), customer.getRole(), customer.getCategories(),customer.getImageUrl(), customer.getProfileId(), customer.getConfirmation());
        return true;
    }


    public boolean exists(Long id)
    {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM "+customersTableName+" WHERE id = ?", Integer.class, id);
        return cnt != null && cnt > 0;
    }

    public boolean existsAnother(String id,String from, Long user)
    {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM "+customersTableName+" WHERE "+" "+from+" = ? AND id != ?", Integer.class, id, user);
        return cnt != null && cnt > 0;
    }

    public void delete(String value,String pole){
        String SQL = "delete from "+customersTableName+" where "+pole+" = ?";
        template.update(SQL, value);
        return;
    }

    public void delete(Customer customer){

            String SQL = "delete from "+customersTableName+" where mail = ?";
            template.update(SQL, customer.getMail());
    }

    public boolean exists(Customer customer)
    {
        Integer cnt = template.queryForObject(
                "SELECT count(*) FROM "+customersTableName+" WHERE login = ? AND id != ?"
                ,Integer.class
                , customer.getLogin()
                , customer.getId());
        return ((cnt != null && cnt > 0) && this.existsAnother(customer.getMail(),"mail", customer.getId()));
    }

    public Customer getCustomerByMail (String mail){
        try {
            String SQL = "select * from "+customersTableName+" where mail = ?";
            return template.queryForObject(SQL,
                    new Object[]{mail}, new CustomerMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    public Customer getCustomerByProfile (Long mail){
        try {
            String SQL = "select * from "+customersTableName+" where profileId = ?";
            return template.queryForObject(SQL,
                    new Object[]{mail}, new CustomerMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public Customer getCustomerByConfirmation (String mail){
        try {
            String SQL = "select * from "+customersTableName+" where confirmation = ?";
            return template.queryForObject(SQL,
                    new Object[]{mail}, new CustomerMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public Customer getCustomerByLogin(String login) {
        try {
            String SQL = "select * from "+customersTableName+" where login = ?";
            return template.queryForObject(SQL,
                    new Object[]{login}, new CustomerMapper());
        }catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    public Customer getCustomerById(Long id) {
        try {
            String SQL = "select * from "+customersTableName+" where id = ?";
            return template.queryForObject(SQL,
                    new Object[]{id}, new CustomerMapper());
        }catch(EmptyResultDataAccessException ex)
        {

            return null;
        }
    }


    public int getSize()
    {
        return template.queryForList("select * from "+customersTableName).size();
    }

    public boolean updateRecord(Customer customer)
    {
        if(exists(customer.getId())) {
            String SQL = "UPDATE " + customersTableName + " SET name = ?, surname = ?, adress = ?, mail = ?, login = ?, password = ?, role = ?, categories = ?, imageUrl = ?, profileId = ?, confirmation = ?  WHERE id= ?;";
            template.update(SQL, customer.getName(), customer.getSurname(), customer.getAdress(), customer.getMail(), customer.getLogin(), customer.getPassword(), customer.getRole(),customer.getCategories(), customer.getImageUrl(), customer.getProfileId(), customer.getConfirmation(), customer.getId());
            return true;
        }
        return false;
        }

        public ArrayList<Customer> getCustomersWithCategory(Category category)
        {
            try {
                String SQL = "select * from "+customersTableName+" where categories LIKE '%"+category.getId()+"%' ORDER BY id   ";
                return (ArrayList<Customer>)template.query(SQL,
                         new RowMapperResultSetExtractor<Customer>(new CustomerMapper()));
            }catch(EmptyResultDataAccessException ex)
            {
                return null;
            }


        }


}
