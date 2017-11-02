package com.example.daoLayer.daos;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;

public abstract class DAO {
  protected JdbcTemplate template;

  DAO(@Nonnull final JdbcTemplate template) {
    this.template = template;
  }

  public abstract void createTable();
}
