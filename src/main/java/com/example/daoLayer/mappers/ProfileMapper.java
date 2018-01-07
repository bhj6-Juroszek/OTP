package com.example.daoLayer.mappers;

import com.example.daoLayer.entities.Profile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bartek on 2017-03-10.
 */
public class ProfileMapper implements RowMapper<Profile> {
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Profile profile = new Profile();
        profile.setId(rs.getString("profileId"));
        profile.setOwnerId(rs.getString("profileOwnerId"));
        profile.setContent(rs.getString("profileContent"));
        profile.setShowMail(rs.getBoolean("showMail"));
        profile.setShowAddress(rs.getBoolean("showAddress"));
        profile.setFacebookLink(rs.getString("facebookLink"));
        profile.setLinkedInLink("linkedinLink");
        return profile;
    }
}
