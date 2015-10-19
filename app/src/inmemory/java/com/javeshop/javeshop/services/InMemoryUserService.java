package com.javeshop.javeshop.services;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.services.entities.UserDetails;
import com.squareup.otto.Subscribe;

import java.util.Random;

/**
 * Created by Jeffrey Torres on 18/10/2015.
 */
public class InMemoryUserService extends BaseInMemoryService
{
    public InMemoryUserService(JaveShopApplication application)
    {
        super(application);
    }

    @Subscribe
    public void getUserDetails(Users.GetUserDetailsRequest request)
    {
        Users.GetUserDetailsResponse response = new Users.GetUserDetailsResponse();
        Random random = new Random();

        response.userDetails = new UserDetails(request.userId,
                "Vendedor",
                Integer.toString(request.userId),
                "http://www.gravatar.com/avatar/" + Integer.toString(random.nextInt()) + "?d=identicon&s=600",
                5.0f);

        postDelayed(response);
    }
}
