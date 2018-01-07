package com.example.backend.controllers;

import com.example.backend.controllersEntities.responses.ProfileWithUserResponse;
import com.example.backend.helpers.ProfilesManager;
import com.example.backend.helpers.UserManager;
import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@CrossOrigin
@Controller
@RequestMapping("/profile/")
public class ProfileController {

  private ProfilesManager profilesManager;
  private UserManager userManager;

  @Nullable
  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  @ResponseBody public ProfileWithUserResponse getUserProfile(@PathVariable String userId) {
    final ProfileWithUserResponse response = new ProfileWithUserResponse();
    final User user = userManager.getUser(userId);
    if(user == null) {
      return response;
    }
    user.setPassword(null);
    user.setConfirmation(null);
    response.setUser(user);
    final Profile profile = profilesManager.getUserProfile(userId);
    response.setProfile(profile);
    return response;
  }

  @Autowired
  public void setProfilesManager(final ProfilesManager profilesManager) {
    this.profilesManager = profilesManager;
  }

  @Autowired
  public void setUserManager(final UserManager userManager) {
    this.userManager = userManager;
  }
}
