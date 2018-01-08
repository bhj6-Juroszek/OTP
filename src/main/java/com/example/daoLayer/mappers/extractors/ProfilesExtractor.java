package com.example.daoLayer.mappers.extractors;

import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.entities.Rate;
import com.example.daoLayer.mappers.ProfileMapper;
import com.example.daoLayer.mappers.RateMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProfilesExtractor implements ResultSetExtractor<List<Profile>> {

  @Override
  public List<Profile> extractData(final ResultSet rs) throws SQLException, DataAccessException {
    final ProfileMapper profileMapper = new ProfileMapper();
    final RateMapper rateMapper = new RateMapper();
    final Map<Profile, String> resultMap = new HashMap();
    while (rs.next()) {
      final Profile profile = profileMapper.mapRow(rs, 0);
      final String link = rs.getString("value");
      final Optional<Rate> rate = rateMapper.mapRow(rs, 0);
      resultMap.putIfAbsent(profile, "");
      resultMap.computeIfPresent(profile, (key, value) -> {
        if(link != null) {
          key.getSocialMediaLinks().add(link);
        }
        rate.ifPresent(found -> key.getRates().add(found));
        return "";
      });
    }
    return new ArrayList<>(resultMap.keySet());
  }
}
