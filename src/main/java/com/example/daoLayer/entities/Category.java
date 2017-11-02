package com.example.daoLayer.entities;

import javax.persistence.*;

/**
 * Created by Bartek on 2017-03-11.
 */
public class Category {
    private long id;
    private String name;
    private long parent;

    public Category() {
    }

    public Category(final String name, final long parent) {
        this.name = name;
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(final long parent) {
        this.parent = parent;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
