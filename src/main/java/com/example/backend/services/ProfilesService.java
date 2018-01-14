package com.example.backend.services;

import com.example.daoLayer.daos.ProfilesDAO;
import com.example.daoLayer.entities.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Created by Bartek on 2017-03-10.
 */
@Service
public class ProfilesService {

  private ProfilesDAO profilesDAO;

//  public void changeProfileText(@Nonnull final User user, @Nonnull final  String text) {
//    Profile profile = getUserProfile(user.getId());
//    profile.setContent(text);
//    profilesDAO.updateProfile(profile);
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

  public boolean updateProfile(@Nonnull final Profile profile) {
    final Profile profileDao = profilesDAO.getProfileByUser(profile.getOwnerId());
    if(profileDao != null && Objects.equals(profile, profileDao)) {
      profilesDAO.updateProfile(profile);
      return true;
    }
    return false;
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
