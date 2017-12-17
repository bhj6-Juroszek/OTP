package com.example.daoLayer.daos;

import com.example.daoLayer.AsyncDbSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Nonnull;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class DAO {
  protected final JdbcTemplate template;
  final AsyncDbSaver asyncSaver;
  final NamedParameterJdbcTemplate parameterJdbcTemplate;

  @Autowired
  DAO(@Nonnull final JdbcTemplate template, @Nonnull final AsyncDbSaver asyncDbSaver) {
    this.template = template;
    this.asyncSaver = asyncDbSaver;
    this.parameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
  }

  public abstract void createTable();

  protected boolean tableExists(@Nonnull final String tableName) {
    try {
      final DatabaseMetaData meta = template.getDataSource().getConnection().getMetaData();
      final ResultSet res = meta.getTables(null, null, tableName,
          new String[] {"TABLE"});
      return res.next();
    } catch (SQLException ignored) {
      return false;
    }
  }
}
