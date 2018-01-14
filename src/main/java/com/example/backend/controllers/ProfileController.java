package com.example.backend.controllers;

import com.example.backend.controllersEntities.responses.ProfileWithUserResponse;
import com.example.backend.services.ProfilesService;
import com.example.backend.services.UsersService;
import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@CrossOrigin
@Controller
@RequestMapping("/profile/")
public class ProfileController extends AuthenticatedController {

  private ProfilesService profilesService;
  private UsersService usersService;

  @Nullable
  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  @ResponseBody public ProfileWithUserResponse getUserProfile(@PathVariable String userId) {
    final ProfileWithUserResponse response = new ProfileWithUserResponse();
    final User user = usersService.getUser(userId);
    if(user == null) {
      return response;
    }
    user.setPassword(null);
    user.setConfirmation(null);
    response.setUser(user);
    final Profile profile = profilesService.getUserProfile(userId);
    response.setProfile(profile);
    return response;
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
  @ResponseBody public boolean updateUserProfile(@RequestBody Profile profile, @RequestParam String uuid) {
    // Just sanity checks, but if it returns false somebody manipulated request data
    final User loggedUser = manager.getLoggedUsers().get(uuid).getUser();
    if(loggedUser != null && loggedUser.getId().equals(profile.getOwnerId())) {
      return profilesService.updateProfile(profile);
    }
    return false;
  }

  @Autowired
  public void setProfilesService(final ProfilesService profilesService) {
    this.profilesService = profilesService;
  }

  @Autowired
  public void setUsersService(final UsersService usersService) {
    this.usersService = usersService;
  }
}
