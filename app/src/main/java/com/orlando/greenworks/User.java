package com.orlando.greenworks;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class User {

    private String userID;
    private String profileImagePath;
    private String firstName;
    private String lastName;
    private String address;
    private String suite;
    private String email;
    private String phoneNumber;
    private String password;

    public User() {
    }

    // Constructor for creating a new user
    public User(String firstName, String lastName, String address, String suite, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.suite = suite;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // for getting current user
    public User(String userID, String firstName, String lastName, String address, String email, String phoneNumber) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String userID, String profileImagePath, String firstName, String lastName, String address, String email, String phoneNumber, String password) {
        this.userID = userID;
        this.profileImagePath = profileImagePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
