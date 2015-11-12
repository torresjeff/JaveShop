package com.javeshop.javeshop.services;

import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;

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
}
