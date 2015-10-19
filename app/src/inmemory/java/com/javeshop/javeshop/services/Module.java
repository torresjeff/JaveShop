package com.javeshop.javeshop.services;

import android.util.Log;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;

/**
 * Created by Jeffrey Torres on 11/10/2015.
 */
public class Module
{
    public static void register (JaveShopApplication application)
    {
        Log.e("MODULE", "In memory register method called");
        new InMemoryAccountService(application);
        new InMemoryProductService(application);
        new InMemoryUserService(application);
    }
}
