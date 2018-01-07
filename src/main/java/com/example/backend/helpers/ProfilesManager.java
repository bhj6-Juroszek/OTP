package com.example.backend.helpers;

import com.example.daoLayer.daos.ProfilesDAO;
import com.example.daoLayer.entities.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Bartek on 2017-03-10.
 */
@Component
public class ProfilesManager {

  private ProfilesDAO profilesDAO;

//  public void changeProfileText(@Nonnull final User user, @Nonnull final  String text) {
//    Profile profile = getUserProfile(user.getId());
//    profile.setContent(text);
//    profilesDAO.updateRecord(profile);
//  }

  @Nullable
  public Profile getUserProfile(@Nonnull final String userId) {
    final Profile profile = profilesDAO.getProfileByUser(userId);

    // we make sure in Controller that user exists
    if(profile == null) {
      return createProfile(userId);
    }
    return profile;
  }

  public Profile createProfile(@Nonnull final String userId) {
    final Profile newProfile = new Profile();
    newProfile.setOwnerId(userId);
    profilesDAO.saveToDB(newProfile);
    return newProfile;
  }

  @Autowired
  public void setProfilesDAO(final ProfilesDAO profilesDAO) {
    this.profilesDAO = profilesDAO;
  }
}
