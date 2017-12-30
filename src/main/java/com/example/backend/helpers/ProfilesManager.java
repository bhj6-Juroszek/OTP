package com.example.backend.helpers;

import com.example.daoLayer.daos.ProfilesDAO;
import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

/**
 * Created by Bartek on 2017-03-10.
 */
public class ProfilesManager {

  private final ProfilesDAO profilesDAO;

  @Autowired
  public ProfilesManager(@Nonnull final ProfilesDAO profilesDAO) {
    this.profilesDAO = profilesDAO;
  }

  private Profile getUserProfile(User user) {
    return profilesDAO.getProfileByUser(user.getId());
  }

  public void changeProfileText(User user, String text) {
    Profile profile = getUserProfile(user);
    profile.setContent(text);
    profilesDAO.updateRecord(profile);
  }

  public void createProfile(User user) {
    Profile newProfile = new Profile(user.getId(), "");
    profilesDAO.saveToDB(newProfile);
  }

}
