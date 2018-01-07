package com.example.utils;

import com.example.daoLayer.daos.PlacesDAO;
import com.example.daoLayer.entities.Country;
import com.example.daoLayer.entities.Place;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Scanner;

// Class to load cities and countries from resource files to DB

@Component
public class PlacesLoader {

  public void saveCountries(@Nonnull final PlacesDAO dao) {
    final InputStream inputStream =
        PlacesLoader.class.getClassLoader().getResourceAsStream("countrycodes.csv");

    try (Scanner scanner = new Scanner(inputStream)) {
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] tokens = line.split(",");
        final Country country = new Country();
        country.setCountryName(tokens[0].replace('"', ' ').trim());
        country.setCountryId(tokens[1].toLowerCase());
        dao.saveToDB(country);
      }
    }
  }

  public void saveCities(@Nonnull final PlacesDAO dao) {
    final InputStream inputStream =
        PlacesLoader.class.getClassLoader().getResourceAsStream("cities.txt");

    try (Scanner scanner = new Scanner(inputStream)) {
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] tokens = line.split(",");
        final Place place = new Place();
        place.setCountryCode(tokens[0].toLowerCase());
        final StringBuilder cityNameBuilder = new StringBuilder();
        cityNameBuilder.append((char)(tokens[1].charAt(0)-32));
        cityNameBuilder.append(tokens[1].substring(1));
        place.setName(cityNameBuilder.toString());
        place.setLat(Double.valueOf(tokens[5]));
        place.setLng(Double.valueOf(tokens[6]));
        dao.saveToDB(place);
      }
    }
  }
}
