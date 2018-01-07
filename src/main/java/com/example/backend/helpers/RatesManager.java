package com.example.backend.helpers;

/**
 * Created by Bartek on 2017-03-11.
 */
public class RatesManager {

//  private final RatesDAO ratesDAO;

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
