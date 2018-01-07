package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.TrainingInstance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainingInstanceMapper implements RowMapper<TrainingInstance> {

  @Override
  public TrainingInstance mapRow(final ResultSet resultSet, final int i) throws SQLException {
    final String id = resultSet.getString("trainingsInsId");
    final TrainingInstance trainingInstance = new TrainingInstance();
    trainingInstance.setId(id);
    trainingInstance.setDateEnd(resultSet.getTimestamp("trainingInsDateEnd"));
    trainingInstance.setDateStart(resultSet.getTimestamp("trainingInsDateStart"));
    trainingInstance.setTrainingParent(resultSet.getString("idTrainings"));
    return trainingInstance;
  }

}
