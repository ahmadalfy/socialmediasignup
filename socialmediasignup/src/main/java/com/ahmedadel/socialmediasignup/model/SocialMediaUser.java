package com.ahmedadel.socialmediasignup.model;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * SocialMediaUser is the mapper model that will return from sign up with our social media platforms we integrated with.
 */

public class SocialMediaUser {

    private String userId;
    private String accessToken;
    private String profilePictureUrl;
    private String username;
    private String fullName;
    private String email;
    private String pageLink;

    public SocialMediaUser() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialMediaUser that = (SocialMediaUser) o;

        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"SocialMediaUser\":{"
                + "\"userId\":\"" + userId + "\""
                + ", \"accessToken\":\"" + accessToken + "\""
                + ", \"profilePictureUrl\":\"" + profilePictureUrl + "\""
                + ", \"username\":\"" + username + "\""
                + ", \"fullName\":\"" + fullName + "\""
                + ", \"email\":\"" + email + "\""
                + ", \"pageLink\":\"" + pageLink + "\""
                + "}}";
    }
}
