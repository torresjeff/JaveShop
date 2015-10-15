package com.javeshop.javeshop.infrastructure;

/**
 * Created by Jeffrey Torres on 11/10/2015.
 */
public class User
{
    private int id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String email;
    private String phoneNumber;

    private boolean isLoggedIn;
    private float balance;
    private float reputation;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isLoggedIn()
    {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn)
    {
        this.isLoggedIn = isLoggedIn;
    }

    public float getBalance()
    {
        return balance;
    }

    public void setBalance(float balance)
    {
        this.balance = balance;
    }

    public float getReputation()
    {
        return reputation;
    }

    public void setReputation(float reputation)
    {
        this.reputation = reputation;
    }
}
