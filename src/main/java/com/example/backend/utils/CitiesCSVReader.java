package com.example.backend.utils;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.entities.City;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CitiesCSVReader {

  public static void readCities() {
    final Set<String> cities = new HashSet<>();
    final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
    String csvFile = "/home/miasta.csv";
    String line;
    String cvsSplitBy = ";";
    int a = 0;
    try (final BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(csvFile), "UTF-8"))) {
      br.readLine();
      while ((line = br.readLine()) != null) {
        String[] country = line.split(cvsSplitBy);
        if (country.length > 7) {
          final City city = new City(country[6], 1);
          if (!cities.contains(city.getName())) {
            cities.add(city.getName());
            executor.submit(() -> {
              System.out.println("City [ name=" + country[6] + "]");
              DAOHandler.citiesDAO.saveToDB(city);

            });
            a++;
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(a);

  }

}
