package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Rate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-03-10.
 */
public class RateMapper implements RowMapper<Rate> {

    public Rate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rate rate = new Rate();
        rate.setId(rs.getInt("id"));
        rate.setComment(rs.getString("comment"));
        rate.setValue(rs.getInt("value"));
        rate.setProfileId(rs.getLong("profileId"));
        rate.setFromId(rs.getLong("fromId"));
        rate.setDate(rs.getDate("date"));
        return rate;

    }
}
