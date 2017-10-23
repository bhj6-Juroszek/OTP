package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Profile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-03-10.
 */
public class ProfileMapper implements RowMapper<Profile> {
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile prof = new Profile();
        prof.setId(rs.getInt("id"));
        prof.setUserId(rs.getInt("userId"));
        prof.setText(rs.getString("text"));
        return prof;

    }
}
