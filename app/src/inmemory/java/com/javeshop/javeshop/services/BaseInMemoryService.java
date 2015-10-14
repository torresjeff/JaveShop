package com.javeshop.javeshop.services;

import android.os.Handler;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.squareup.otto.Bus;

import java.util.Random;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class BaseInMemoryService
{
    protected final Bus bus;
    protected final JaveShopApplication application;
    protected final Handler handler;
    protected final Random random;

    protected BaseInMemoryService(JaveShopApplication application)
    {
        this.application = application;
        this.bus = application.getBus();
        handler = new Handler();
        random = new Random();
        bus.register(this);
    }

    protected void invokeDelayed(Runnable runnable, long millisecondMin, long millisecondMax)
    {
        if (millisecondMin > millisecondMax)
        {
            throw new IllegalArgumentException("Min must be smaller than max");
        }

        long delay = (long)(random.nextDouble() * (millisecondMax - millisecondMin)) + millisecondMin;
        handler.postDelayed(runnable, delay);
    }

    protected void postDelayed(final Object event, long millisecondMin, long millisecondMax)
    {
        invokeDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                bus.post(event);
            }
        }, millisecondMin, millisecondMax);
    }

    protected void postDelayed(Object event, long milliseconds)
    {
        postDelayed(event, milliseconds, milliseconds);
    }

    protected void postDelayed(Object event)
    {
        postDelayed(event, 600, 1200);
    }
}
