package com.example.model;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.daos.ProfilesDAO;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.entities.Profile;

/**
 * Created by Bartek on 2017-03-10.
 */
public class ProfilesManager {

    final ProfilesDAO profilesDAO = DAOHandler.profilesDAO;

    public Profile getUserProfile(User user)
    {
        return profilesDAO.getProfileByUser(user.getId());
    }

    public void changeProfileText(User user, String text)
    {
        Profile profile=getUserProfile(user);
        profile.setText(text);
        profilesDAO.updateRecord(profile);
    }

    public void createProfile(User user)
    {
        Profile newProfile=new Profile(user.getId(),"");
        profilesDAO.saveToDB(newProfile);
    }


}
