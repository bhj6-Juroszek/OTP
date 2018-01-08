package com.example.daoLayer.mappers.extractors;

import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.TrainingInstance;
import com.example.daoLayer.entities.TrainingReservation;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.mappers.TrainingMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrainingWithInstancesExtractor implements ResultSetExtractor<List<Training>> {

  private static final String PLACEHOLDER = "";

  @Override
  public List<Training> extractData(final ResultSet rs) throws SQLException, DataAccessException {

    final TrainingMapper trainingMapper = new TrainingMapper();
    final Map<Training, String> resultMap = new HashMap();
    final Map<TrainingInstance, Training> trainingInstancesMap = new HashMap();
    while (rs.next()) {
      final Training training = trainingMapper.mapRow(rs, 0);
      final Optional<TrainingInstance> trainingInstance = mapTrainingInstance(rs);
      final Optional<TrainingReservation> trainingReservation = mapTrainingReservation(rs);
      resultMap.putIfAbsent(training, PLACEHOLDER);
      trainingInstance.ifPresent(trainingInstanceValue -> resultMap.computeIfPresent(training, (key, value) -> {
        trainingInstancesMap.putIfAbsent(trainingInstanceValue, key);
        trainingInstancesMap.computeIfPresent(trainingInstanceValue, (instanceKey, instanceValue) -> {
          trainingReservation
              .ifPresent(trainingReservation1 -> instanceKey.getTrainingReservations().add(trainingReservation1));
          return key;
        });
        return PLACEHOLDER;
      }));
    }
    for (Map.Entry<TrainingInstance, Training> e : trainingInstancesMap.entrySet()) {
      resultMap.computeIfPresent(e.getValue(), (key, value) -> {
        key.getInstances().add(e.getKey());
        return value;
      });
    }
    return new ArrayList<>(resultMap.keySet());
  }

  private Optional<TrainingReservation> mapTrainingReservation(@Nonnull final ResultSet rs) throws SQLException {
    final String id = rs.getString("trainingsResId");
    if (id == null) {
      return Optional.empty();
    }
    final TrainingReservation trainingReservation = new TrainingReservation();
    trainingReservation.setId(id);
    trainingReservation.setTrainingInstance(rs.getString("idTrainingsIns"));
    final User customer = mapCustomer(rs);
    trainingReservation.setCustomer(customer);
    return Optional.of(trainingReservation);
  }

  private Optional<TrainingInstance> mapTrainingInstance(@Nonnull final ResultSet rs) throws SQLException {
    final String id = rs.getString("trainingsInsId");
    if (id == null) {
      return Optional.empty();
    }
    final TrainingInstance trainingInstance = new TrainingInstance();
    trainingInstance.setId(id);
    trainingInstance.setDateEnd(rs.getTimestamp("trainingInsDateEnd"));
    trainingInstance.setDateStart(rs.getTimestamp("trainingInsDateStart"));
    trainingInstance.setTrainingParent(rs.getString("idTrainings"));
    return Optional.of(trainingInstance);
  }

  private User mapCustomer(@Nonnull final ResultSet rs) throws SQLException {
    final String id = rs.getString("c_userId");
    if (id == null) {
      return null;
    }
    final User user = new User();
    user.setId(id);
    user.setName(rs.getString("c_userName"));
    user.setAdress(rs.getString("c_adress"));
    user.setMail(rs.getString("c_mail"));
    user.setImageUrl(rs.getString("c_imageUrl"));
    return user;
  }
}
