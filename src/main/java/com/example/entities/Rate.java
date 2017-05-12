package com.example.entities;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Bartek on 2017-03-10.
 */
@Entity
@Table(name="ratesTable")
public class Rate {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private String comment;
    @Column
    private int value;
    @Column
    private long profileId;
    @Column
    private long fromId;
    @Column
    private Date date;

    public Rate(final String comment, final int value, final long profileId, final long fromId, final Date date) {
        this.comment = comment;
        this.value = value;
        this.profileId = profileId;
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

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(final long profileId) {
        this.profileId = profileId;
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
