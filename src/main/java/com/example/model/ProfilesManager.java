package com.example.model;

import com.example.daoLayer.daos.CustomersDAO;
import com.example.daoLayer.daos.ProfilesDAO;
import com.example.daoLayer.entities.Customer;
import com.example.daoLayer.entities.Profile;

/**
 * Created by Bartek on 2017-03-10.
 */
public class ProfilesManager {

    ProfilesDAO profilesRep=ProfilesDAO.getInstance();
    CustomersDAO customersRep=CustomersDAO.getInstance();

    public Profile getUserProfile(Customer customer)
    {
        return profilesRep.getProfileByUser(customer.getId());
    }

    public void changeProfileText(Customer customer, String text)
    {
        Profile profile=getUserProfile(customer);
        profile.setText(text);
        profilesRep.updateRecord(profile);
    }

    public void createProfile(Customer customer)
    {
        Profile newProfile=new Profile(customer.getId(),"");
        profilesRep.saveToDB(newProfile);
        customer.setProfileId(profilesRep.getProfileByUser(customer.getId()).getId());
        customersRep.updateRecord(customer);
    }


}
