package nz.ac.otago.flatty.domain;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Accounts implements Serializable {
    private Integer accountID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private byte[] photoEncoded;
    private Details account_details;
    private HashSet<Integer> matches = new HashSet<>();

    public Accounts(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Accounts(Integer accountID, String firstName, String lastName, String email, String password) {
        this.accountID = accountID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Accounts(Integer accountID, String firstName, String lastName, String email, String password, byte[] photoEncoded) {
        this.accountID = accountID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.photoEncoded = photoEncoded;
    }

    public Accounts(Integer accountID, String firstName, String lastName, String email, String password, HashSet<Integer> matches) {
        this.accountID = accountID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.matches = matches;
    }

    public Accounts (Integer accountID, String firstName, String lastName, String email, String password, byte[] photoEncoded, HashSet<Integer> matches) {
        this.accountID = accountID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.photoEncoded = photoEncoded;
        this.matches = matches;
    }

    public Accounts() {
    }

    public Integer getAccountID() {

        return accountID;
    }

    public void setAccountID(Integer accountID) {

        this.accountID = accountID;
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

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public byte[] getPhoto() {

        return photoEncoded;
    }

    public void setPhoto(byte[] photoEncoded) {

        this.photoEncoded = photoEncoded;
    }

    public Details getDetails() {

        return account_details;
    }

    public void setDetails(Details account_details) {

        this.account_details = account_details;
    }

    public HashSet<Integer> getMatches() {

        return matches;
    }

    public void setMatches(HashSet<Integer> matches) {

        this.matches = matches;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "accountID=" + accountID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
