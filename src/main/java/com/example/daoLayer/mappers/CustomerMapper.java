package com.example.daoLayer.mappers;


import com.example.daoLayer.entities.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
public class CustomerMapper implements RowMapper<Customer> {



    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer cust = new Customer();
        cust.setId(rs.getInt("id"));
        cust.setName(rs.getString("name"));
        cust.setSurname(rs.getString("surname"));
        cust.setAdress(rs.getString("adress"));
        cust.setMail(rs.getString("mail"));
        cust.setLogin(rs.getString("login"));
        cust.setPassword(rs.getString("password"));
        cust.setRole(rs.getBoolean("role"));
        cust.setCategories(rs.getString("categories"));
        cust.setImageUrl(rs.getString("imageUrl"));
        cust.setConfirmation(rs.getString("confirmation"));
        cust.setProfileId(rs.getInt("profileId"));
        return cust;

    }
}
