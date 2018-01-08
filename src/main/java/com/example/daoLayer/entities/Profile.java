package com.example.daoLayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartek on 2017-03-10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile extends Entity {

  private String ownerId;
  private String content;
  private boolean showMail;
  private boolean showAddress;
  private String facebookLink;
  private String linkedInLink;
  private String phoneNumber;
  private List<String> socialMediaLinks;
  private List<Rate> rates;

  public Profile() {
    showMail = false;
    showAddress = false;
    socialMediaLinks = new ArrayList<>();
    rates = new ArrayList<>();
  }



  public Profile(final String ownerId, final String content, final boolean showMail, final boolean showAddress,
      final String facebookLink,
      final String linkedInLink, final List<String> socialMediaLinks) {
    this.ownerId = ownerId;
    this.content = content;
    this.showMail = showMail;
    this.showAddress = showAddress;
    this.facebookLink = facebookLink;
    this.linkedInLink = linkedInLink;
    this.socialMediaLinks = socialMediaLinks;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(final String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public List<Rate> getRates() {
    return rates;
  }

  public void setRates(final List<Rate> rates) {
    this.rates = rates;
  }

  public boolean isShowMail() {
    return showMail;
  }

  public void setShowMail(final boolean showMail) {
    this.showMail = showMail;
  }

  public boolean isShowAddress() {
    return showAddress;
  }

  public void setShowAddress(final boolean showAddress) {
    this.showAddress = showAddress;
  }

  public String getFacebookLink() {
    return facebookLink;
  }

  public void setFacebookLink(final String facebookLink) {
    this.facebookLink = facebookLink;
  }

  public String getLinkedInLink() {
    return linkedInLink;
  }

  public void setLinkedInLink(final String linkedInLink) {
    this.linkedInLink = linkedInLink;
  }

  public List<String> getSocialMediaLinks() {
    return socialMediaLinks;
  }

  public void setSocialMediaLinks(final List<String> socialMediaLinks) {
    this.socialMediaLinks = socialMediaLinks;
  }



  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(final String ownerId) {
    this.ownerId = ownerId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }
}
