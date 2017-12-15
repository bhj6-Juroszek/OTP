package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.TrainingInstance;
import com.example.daoLayer.entities.TrainingReservation;
import com.example.daoLayer.entities.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingWithInstancesExtractor implements ResultSetExtractor<List<Training>> {

  private static final String PLACEHOLDER = "";

  @Override
  public List<Training> extractData(final ResultSet rs) throws SQLException, DataAccessException {

    final TrainingMapper trainingMapper = new TrainingMapper();
    final Map<Training, String> resultMap = new HashMap();
    final Map<TrainingInstance, Training> trainingInstancesMap = new HashMap();
    while (rs.next()) {
      final Training training = trainingMapper.mapRow(rs, 0);
      final TrainingInstance trainingInstance = mapTrainingInstance(rs);
      final TrainingReservation trainingReservation = mapTrainingReservation(rs);
      resultMap.putIfAbsent(training, PLACEHOLDER);
      resultMap.computeIfPresent(training, (key, value) -> {
        trainingInstancesMap.putIfAbsent(trainingInstance, key);
        trainingInstancesMap.computeIfPresent(trainingInstance, (instanceKey, instanceValue) -> {
          instanceKey.getTrainingReservations().add(trainingReservation);
          return key;
        });
        return PLACEHOLDER;
      });
    }
    for (Map.Entry<TrainingInstance, Training> e : trainingInstancesMap.entrySet()) {
      e.getValue().getInstances().add(e.getKey());
    }
    return new ArrayList<>(resultMap.keySet());
  }

  private TrainingReservation mapTrainingReservation(@Nonnull final ResultSet rs) throws SQLException {
    final long id = rs.getLong("trainingsResId");
    if (id == 0) {
      return null;
    }
    final TrainingReservation trainingReservation = new TrainingReservation();
    trainingReservation.setId(id);
    trainingReservation.setTrainingInstance(rs.getLong("idTrainingsIns"));
    final User customer = mapCustomer(rs);
    trainingReservation.setCustomer(customer);
    return trainingReservation;
  }

  private TrainingInstance mapTrainingInstance(@Nonnull final ResultSet rs) throws SQLException {
    final long id = rs.getLong("trainingsInsId");
    if (id == 0) {
      return null;
    }
    final TrainingInstance trainingInstance = new TrainingInstance();
    trainingInstance.setId(id);
    trainingInstance.setDateEnd(rs.getTimestamp("trainingInsDateEnd"));
    trainingInstance.setDateStart(rs.getTimestamp("trainingInsDateStart"));
    trainingInstance.setTrainingParent(rs.getLong("idTrainings"));
    return trainingInstance;
  }

  private User mapCustomer(@Nonnull final ResultSet rs) throws SQLException {
    User user = new User();
    user.setId(rs.getInt("c_userId"));
    user.setName(rs.getString("c_userName"));
    user.setAdress(rs.getString("c_adress"));
    user.setMail(rs.getString("c_mail"));
    user.setImageUrl(rs.getString("c_imageUrl"));
    return user;
  }
}