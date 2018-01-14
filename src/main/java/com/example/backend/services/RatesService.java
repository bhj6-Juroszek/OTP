package com.example.backend.services;

import com.example.backend.controllersEntities.responses.TrainingsResponse;
import com.example.daoLayer.daos.RatesDAO;
import com.example.daoLayer.daos.TrainingsDAO;
import com.example.daoLayer.entities.Rate;
import com.example.daoLayer.entities.User;
import com.example.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Date;

import static com.example.utils.ResponseCode.GENERAL_FAIL;
import static com.example.utils.ResponseCode.SUCCESS;

/**
 * Created by Bartek on 2017-03-11.
 */
@Service
public class RatesService {

  private final RatesDAO ratesDAO;
  private final TrainingsDAO trainingsDAO;
  private final SessionManager sessionManager;

  @Autowired
  public RatesService(@Nonnull final RatesDAO ratesDAO, @Nonnull final TrainingsDAO trainingsDAO,
      @Nonnull final SessionManager sessionManager) {
    this.ratesDAO = ratesDAO;
    this.trainingsDAO = trainingsDAO;
    this.sessionManager = sessionManager;
  }

  public TrainingsResponse getTrainingsToRate(@Nonnull final String uuid) {
    final TrainingsResponse response = new TrainingsResponse();
    response.setResponseCode(SUCCESS);
    final User loggedUser = sessionManager.getLoggedUsers().get(uuid).getUser();
    response.setUserTrainings(trainingsDAO.getTrainingsToRate(loggedUser.getId()));
    return response;
  }

  public int rate(@Nonnull final Rate rate, @Nonnull final String uuid) {
    final User loggedUser = sessionManager.getLoggedUsers().get(uuid).getUser();
    if(validateRate(rate, loggedUser.getId())) {
      rate.setDate(new Date());
      ratesDAO.saveToDB(rate);
      return SUCCESS;
    } else {
      return GENERAL_FAIL;
    }
  }

  private boolean validateRate(@Nonnull final Rate rate, @Nonnull final String userId) {
    return userId.equals(rate.getFromId()) && !ratesDAO.exists(rate);
  }

  //  @Autowired
//  public RatesManager(@Nonnull final RatesDAO ratesDAO) {
//    this.ratesDAO = ratesDAO;
//  }
//
//  public boolean rateProfile(@Nonnull final User fromUser, @Nonnull final Profile ratedProfile, final int value,
//      @Nonnull final String comment) {
//    final Date date = new Date(Calendar.getInstance().getTime().getTime());
//    final Rate result = new Rate(comment, value, ratedProfile.getOwnerId(), fromUser.getId(), date);
//    return ratesDAO.saveToDB(result);
//  }
//
//  public double getProfileAverageRate(@Nonnull final Profile profile) {
//    final List<Rate> rates = ratesDAO.getRatesByProfile(profile.getId());
//    double result = 0;
//    for (Rate r : rates) {
//      result += r.getValue();
//    }
//    result /= rates.size();
//    return result;
//  }
//
//  public ArrayList<Rate> getProfileLastRates(@Nonnull final Profile profile) {
//    List<Rate> rates = ratesDAO.getRatesByProfile(profile.getId());
//    int max = 5;
//    if ((rates.size() + 1 < max)) {
//      max = rates.size() - 1;
//    }
//    return new ArrayList(rates.subList(0, max));
//  }
//
//  public List<Rate> getProfileRates(@Nonnull final Profile profile) {
//    return ratesDAO.getRatesByProfile(profile.getId());
//
//  }
//
//  public Rate ifUserRated(@Nonnull final User user, @Nonnull final Profile profile) {
//    return ratesDAO.getProfileUserRate(profile.getId(), user.getId());
//
//  }

}
