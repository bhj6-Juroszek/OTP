package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity {

  protected String id;

  Entity() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return String.format("Entity: [id:%s]", id);
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if(o != null) {
      if(o instanceof Entity) {
        final Entity entity = (Entity)o;
        return id.equals(((Entity) o).getId());
      }
    }
    return false;
  }
}
