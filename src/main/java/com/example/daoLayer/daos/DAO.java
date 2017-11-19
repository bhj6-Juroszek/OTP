package com.example.daoLayer.daos;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class DAO {
  protected JdbcTemplate template;
  protected final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
  DAO(@Nonnull final JdbcTemplate template) {
    this.template = template;
  }

  public abstract void createTable();
}
