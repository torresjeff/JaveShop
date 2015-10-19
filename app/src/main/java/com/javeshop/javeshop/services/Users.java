package com.javeshop.javeshop.services;

import com.javeshop.javeshop.services.entities.UserDetails;

/**
 * Created by Jeffrey Torres on 18/10/2015.
 */
public class Users
{
    private Users()
    {
    }

    public static class GetUserDetailsRequest
    {
        public int userId;
        public GetUserDetailsRequest(int userId)
        {
            this.userId = userId;
        }
    }

    public static class GetUserDetailsResponse extends ServiceResponse
    {
        public UserDetails userDetails;
    }
}
