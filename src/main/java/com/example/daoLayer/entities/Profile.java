package com.example.daoLayer.entities;

import javax.persistence.*;

/**
 * Created by Bartek on 2017-03-10.
 */
@Entity
@Table(name="profilesTable")
public class Profile {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private long userId;
    @Column
    private String text;

    public Profile(final long userId, final String text) {
        this.userId = userId;
        this.text = text;
    }
    public Profile() {

    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
