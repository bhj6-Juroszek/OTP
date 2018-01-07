package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Created by Bartek on 2017-03-11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends Entity {

  private String name;
  private String parent;
  private boolean theoretical;

  public Category() {
    this("", "0", false);
  }

  public Category(final String name, final String parent, boolean theoretical) {
    super();
    this.name = name;
    this.parent = parent;
   this.theoretical = theoretical;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(final String parent) {
    this.parent = parent;
  }

  public boolean getTheoretical() {
    return theoretical;
  }

  public void setTheoretical(final boolean theoretical) {
    this.theoretical = theoretical;
  }

  @Override
  public String toString() {
    return String.format("Category:[%s]", name);
  }

  @Override
  public boolean equals(Object o) {
    if (o != null) {
      if (o instanceof Category) {
        final Category cat = (Category) o;
        return Objects.equals(this.id, cat.getId());
      }
    }
    return false;
  }
}
