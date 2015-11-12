package com.javeshop.javeshop.services;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.squareup.otto.Bus;

public abstract class BaseLiveService
{
    protected final Bus bus;
    protected final JaveShopWebService api;
    protected final JaveShopApplication application;

    protected BaseLiveService(JaveShopWebService api, JaveShopApplication application)
    {
        this.bus = application.getBus();
        this.api = api;
        this.application = application;
        bus.register(this);
    }
}