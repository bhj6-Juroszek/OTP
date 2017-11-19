package com.example.daoLayer.entities;

import com.example.daoLayer.DAOHandler;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String name;
    private String adress;
    private String mail;
    private String login;
    private String password;
    private boolean role;
    private String imageUrl;
    private String confirmation;



    public User(final String name, final String adress, final String mail, final String login, final String password, final boolean role, final String imageUrl, final String confirmation) {
        this.name = name;
        this.adress = adress;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.role = role;
        this.imageUrl = imageUrl;
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

    public User(final String name,final String adress, final String mail, final String login, final String password, final boolean role, final String confirmation) {
        this.name = name;
        this.adress = adress;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.role = role;
        this.confirmation = confirmation;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }


    public String getAdress() {
        return adress;
    }

    public User setAdress(String adress) {
        this.adress = adress;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public User setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean getRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }


    public User(final String name, final String adress, final String mail, final String login, final String password, final boolean role) {
        this.name = name;
        this.adress = adress;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User()
    {

    }

    @Override
    public int hashCode() {
      return (int)id;
    }

  @Override
  public boolean equals(Object object) {
    if(object == null) return false;
    if(object instanceof User) {
      final User user = (User)object;
      if(this.id == user.getId()) return true;
    }
    return false;
  }

    @Override
    public String toString()
    {
        return (name+" Adress:"+adress+" Mail: "+mail);
    }
}
