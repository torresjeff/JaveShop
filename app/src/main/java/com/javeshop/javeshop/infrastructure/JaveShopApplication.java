package com.javeshop.javeshop.infrastructure;


import android.app.Application;

import com.javeshop.javeshop.services.Module;
import com.squareup.otto.Bus;

/**
 * Created by Jeffrey Torres on 11/10/2015.
 */
public class JaveShopApplication extends Application
{
    private Auth auth;
    private Bus bus;

    public JaveShopApplication()
    {
        bus = new Bus();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        auth = new Auth(this);

        Module.register(this);
    }

    public Auth getAuth()
    {
        return auth;
    }

    public Bus getBus()
    {
        return bus;
    }
}
