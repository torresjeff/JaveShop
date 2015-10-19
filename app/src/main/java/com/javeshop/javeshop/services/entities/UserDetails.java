package com.javeshop.javeshop.services.entities;

/**
 * Created by Jeffrey Torres on 18/10/2015.
 */
public class UserDetails
{
    private int id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private float reputation;

    public UserDetails(int id, String firstName, String lastName, String avatarUrl, float reputation)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.reputation = reputation;
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public float getReputation()
    {
        return reputation;
    }
}
