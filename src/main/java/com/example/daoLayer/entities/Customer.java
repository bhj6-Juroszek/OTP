package com.example.daoLayer.entities;

import javax.persistence.*;

@Entity
@Table(name="customerTable")
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String adress;
    @Column(unique = true, nullable = false)
    private String mail;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private boolean role;
    @Column
    private String categories;
    @Column
    private String imageUrl;
    @Column
    private long profileId;
    @Column
    private String confirmation;


    public Customer(final String name, final String surname, final String adress, final String mail, final String login, final String password, final boolean role, final String categories, final String imageUrl, final long profileId, final String confirmation) {
        this.name = name;
        this.surname = surname;
        this.adress = adress;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.role = role;
        this.categories = categories;
        this.imageUrl = imageUrl;
        this.profileId = profileId;
        this.confirmation = confirmation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isRole() {
        return role;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(final String confirmation) {
        this.confirmation = confirmation;
    }

    public Customer(final String name, final String surname, final String adress, final String mail, final String login, final String password, final boolean role, final String categories, final long profileId, final String confirmation) {
        this.name = name;
        this.surname = surname;
        this.adress = adress;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.role = role;
        this.categories = categories;
        this.profileId = profileId;
        this.confirmation = confirmation;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(final String categories) {
        this.categories = categories;
    }

    public long getId() {
        return id;
    }

    public Customer setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Customer setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getAdress() {
        return adress;
    }

    public Customer setAdress(String adress) {
        this.adress = adress;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public Customer setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Customer setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Customer setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean getRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }


    public Customer(final String name, final String surname, final String adress, final String mail, final String login, final String password, final boolean role, final String categories, final long profileId) {
        this.name = name;
        this.surname = surname;
        this.adress = adress;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.role = role;
        this.categories = categories;
        this.profileId = profileId;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(final long profileId) {
        this.profileId = profileId;
    }

    public Customer ()
    {

    }


    @Override
    public String toString()
    {
        return (name+" "+surname+" "+" Adress:"+adress+" Mail: "+mail);
    }
}
