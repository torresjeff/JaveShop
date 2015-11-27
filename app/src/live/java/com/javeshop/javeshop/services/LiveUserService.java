package com.javeshop.javeshop.services;

import android.util.Log;

import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.infrastructure.RetrofitCallbackPost;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 1/11/2015.
 */
public class LiveUserService extends BaseLiveService
{
    private final Auth auth;

    public LiveUserService(JaveShopWebService api, JaveShopApplication application)
    {
        super(api, application);
        auth = application.getAuth();
    }

    @Subscribe
    public void searchUser(Users.GetUserDetailsRequest request)
    {
        api.searchUser(request.userId, new RetrofitCallbackPost<Users.GetUserDetailsResponse>(Users.GetUserDetailsResponse.class, bus)
        {
            @Override
            protected void onResponse(Users.GetUserDetailsResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void rateUser(Users.RateUserRequest request)
    {
        Log.e("LiveUserService", "Recibio RateUserRequest");
        api.rateUser(request, new RetrofitCallbackPost<Users.RateUserResponse>(Users.RateUserResponse.class, bus)
        {
            @Override
            protected void onResponse(Users.RateUserResponse response)
            {
                bus.post(response);
            }
        });
    }
}
