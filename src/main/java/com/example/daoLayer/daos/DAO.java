package com.example.daoLayer.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class DAO {
  protected JdbcTemplate template;
  @Autowired
  DAO(@Nonnull final JdbcTemplate template) {
    this.template = template;
  }

  public abstract void createTable();
}
