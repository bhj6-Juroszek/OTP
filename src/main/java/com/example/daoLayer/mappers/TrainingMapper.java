package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Training;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-03-23.
 */
public class TrainingMapper implements RowMapper<Training> {
    public Training mapRow(ResultSet rs, int rowNum) throws SQLException {
        Training training = new Training();
        training.setId(rs.getInt("id"));
        training.setDate(rs.getTimestamp("date"));
        training.setLength(rs.getDouble("length"));
        training.setPrice(rs.getInt("price"));
        training.setCity(rs.getString("city"));
        training.setCategory(rs.getInt("category"));
        training.setDescription(rs.getString("description"));
        training.setOwnerId(rs.getInt("ownerId"));
        training.setTakenById(rs.getInt("takenById"));
        return training;

    }
}
