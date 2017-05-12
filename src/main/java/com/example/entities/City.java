package com.example.entities;

/**
 * Created by Bartek on 2017-05-04.
 */
public class City {

    private long id;
    private String name;
    private long countryId;

    public City( final String name, final long countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public City() {
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

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(final long countryId) {
        this.countryId = countryId;
    }
    @Override
    public String toString()
    {
        return this.name;
    }
}
