package com.example.heychat;

public class User {
    private String userName;
    private String Email;
    private String profilePicture;

    public User(){

    }
    public User(String userName, String email, String profilePicture) {
        this.userName = userName;
        Email = email;
        this.profilePicture = profilePicture;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getEmail() {

        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfilePicture() {

        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {

        this.profilePicture = profilePicture;
    }
}

