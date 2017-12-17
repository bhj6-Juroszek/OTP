package com.example.daoLayer.entities;

import java.util.Objects;

/**
 * Created by Bartek on 2017-03-11.
 */
public class Category extends Entity {

  private String name;
  private String parent;

  public Category() {
    this("", "0");
  }

  public Category(final String name, final String parent) {
    super();
    this.name = name;
    this.parent = parent;
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

  @Override
  public String toString() {
    return this.name;
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
