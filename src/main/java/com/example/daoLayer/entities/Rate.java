package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by Bartek on 2017-03-10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rate extends Entity {

  private String comment;
  private int value;
  private String toId;
  private String fromId;
  private Date date;

  public Rate() {
    this("", 0, "", "", null);
  }

  public Rate(final String comment, final int value, final String toId, final String fromId, final Date date) {
    super();
    this.comment = comment;
    this.value = value;
    this.toId = toId;
    this.fromId = fromId;
    this.date = date;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(final String comment) {
    this.comment = comment;
  }

  public int getValue() {
    return value;
  }

  public void setValue(final int value) {
    this.value = value;
  }

  public String getToId() {
    return toId;
  }

  public void setToId(final String toId) {
    this.toId = toId;
  }

  public String getFromId() {
    return fromId;
  }

  public void setFromId(final String fromId) {
    this.fromId = fromId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }
}
