package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Bartek on 2017-03-10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile extends Entity {

  private String ownerId;
  private String content;

  public Profile() {
    this("", "");

  }
  public Profile(final String ownerId, final String content) {
    super();
    this.ownerId = ownerId;
    this.content = content;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(final String ownerId) {
    this.ownerId = ownerId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }
}
