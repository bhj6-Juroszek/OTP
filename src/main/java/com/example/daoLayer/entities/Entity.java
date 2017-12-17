package com.example.daoLayer.entities;

import java.util.UUID;

public abstract class Entity {

  protected String id;

  public Entity() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
