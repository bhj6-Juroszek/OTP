package com.example.backend.utils;

import com.example.daoLayer.entities.Place;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URL;

import static java.lang.Math.*;

/**
 * Created by Bartek on 2017-05-03.
 */
@Component
public class JsonReader {

  private static final double RADIUS = 6378.16;

  private static String readAll(@Nonnull final Reader reader) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = reader.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public double distance(@Nonnull final Place place1, @Nonnull final Place place2) {
    final double lat1 = place1.getLat();
    final double lon1 = place1.getLng();
    final double lat2 = place2.getLat();
    final double lon2 = place2.getLng();
    final double distanceLon = Radians(lon2 - lon1);
    final double distanceLat = Radians(lat2 - lat1);

    final double a = (sin(distanceLat / 2) * sin(distanceLat / 2)) + cos(Radians(lat1)) * cos(Radians(lat2)) *
        (sin(distanceLon / 2) * sin(distanceLon / 2));
    final double angle = 2 * atan2(sqrt(a), sqrt(1 - a));
    return (angle * RADIUS);
  }

  private static double Radians(final double x) {
    return x * PI / 180;
  }

  public Place getPlace(@Nonnull final String placeName) throws JSONException {
    final Place result = new Place();
    if (placeName.equals("") || placeName.equalsIgnoreCase("online")) {
      result.setName("online");
    } else {
      final String attributeUrl = String.format(
          "http://maps.google.com/maps/api/geocode/json?address=%s&sensor" +
              "=false&region=pl",
          placeName);
      final String strippedUrl = StringUtils.stripAccents(attributeUrl);
      final String strippedUrl2 = strippedUrl.replace("ł", "l").replace(" ", "+");
      try (InputStream is = new URL(strippedUrl2).openStream()) {
        final BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        final String jsonText = readAll(rd);
        final JSONObject json = new JSONObject(jsonText);
        final JSONArray results = (JSONArray) json.get("results");
        JSONObject object = results.getJSONObject(0);
        final JSONArray array = object.getJSONArray("address_components");
        result.setName(array.getJSONObject(0).getString("long_name"));
        object = object.getJSONObject("geometry");
        object = object.getJSONObject("location");
        result.setLat((object.getDouble("lat")));
        result.setLng((object.getDouble("lng")));
      } catch (Exception ex) {
        return null;
      }
    }
    return result;
  }

}
