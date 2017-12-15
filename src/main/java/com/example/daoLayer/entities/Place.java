package com.example.daoLayer.entities;

/**
 * Created by Bartek on 2017-05-04.
 */
public class Place {

    private String name;
    private String postalCode;
    private double lat;
    private double lng;

    public Place(final String name, final String postalCode, final double lat, final double lng) {
        this.name = name;
        this.postalCode = postalCode;
        this.lat = lat;
        this.lng = lng;
    }

    public Place() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(final double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(final double lng) {
        this.lng = lng;
    }
}
