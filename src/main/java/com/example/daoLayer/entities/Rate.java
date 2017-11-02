package com.example.daoLayer.entities;

import java.sql.Date;
import javax.persistence.*;

/**
 * Created by Bartek on 2017-03-10.
 */
public class Rate {
    private long id;
    private String comment;
    private int value;
    private long toId;
    private long fromId;
    private Date date;

    public Rate(final String comment, final int value, final long toId, final long fromId, final Date date) {
        this.comment = comment;
        this.value = value;
        this.toId = toId;
        this.fromId = fromId;
        this.date = date;
    }

    public Rate() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
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

    public long getToId() {
        return toId;
    }

    public void setToId(final long toId) {
        this.toId = toId;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(final long fromId) {
        this.fromId = fromId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }
}
