package com.example.daoLayer.mappers.extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialsPathsExtractor implements ResultSetExtractor<List<String>> {

  @Override
  public List<String> extractData(final ResultSet resultSet) throws SQLException, DataAccessException {
    final List<String> resultList = new ArrayList<>();
    while (resultSet.next()) {
      resultList.add(resultSet.getString("path"));
    }
    return resultList;
  }
}
