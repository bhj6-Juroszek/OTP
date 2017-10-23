package com.example.entities;

import com.example.daos.CustomersDAO;

import java.sql.Date;


/**
 * Created by Bartek on 2017-03-23.
 */
@Entity
@Table(name="trainigsTable")
public class Training {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private Date date;
    @Column
    private Double hour;
    @Column
    private Double length;
    @Column
    private long price;
    @Column
    private long category;
    @Column
    private String city;
    @Column
    private String description;
    @Column
    private long profileId;
    @Column
    private long takenById;

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
    public long getPrice() {
        return price;
    }

    public void setPrice(final long price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Double getHour() {
        return hour;
    }

    public void setHour(final Double hour) {
        this.hour = hour;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(final Double length) {
        this.length = length;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(final long category) {
        this.category = category;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(final long profileId) {
        this.profileId = profileId;
    }

    public long getTakenById() {
        return takenById;
    }

    public void setTakenById(final long takenById) {
        this.takenById = takenById;
    }

    public Training() {

    }

    public Customer getOwner()
    {
        return CustomersDAO.getInstance().getCustomerByProfile(profileId);
    }
}
