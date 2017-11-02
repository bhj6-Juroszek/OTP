package com.example.daoLayer.mappers;


import com.example.daoLayer.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
public class UserMapper implements RowMapper<User> {



    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setAdress(rs.getString("adress"));
        user.setMail(rs.getString("mail"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getBoolean("role"));
        user.setImageUrl(rs.getString("imageUrl"));
        user.setConfirmation(rs.getString("confirmation"));
        return user;

    }
}
