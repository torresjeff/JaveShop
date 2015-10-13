package com.javeshop.javeshop.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.views.NavDrawer;
import com.squareup.otto.Bus;

/**
 * Created by Jeffrey Torres on 11/10/2015.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private boolean isRegisteredWithBus;
    protected JaveShopApplication application;
    protected Toolbar toolbar;
    protected NavDrawer navDrawer;
    protected Bus bus;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        application = (JaveShopApplication)getApplication();
        bus = application.getBus();

        bus.register(this);
        isRegisteredWithBus = true;
    }

    @Override
    public void finish()
    {
        super.finish();

        if (isRegisteredWithBus)
        {
            bus.unregister(this);
            isRegisteredWithBus = false;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (isRegisteredWithBus)
        {
            bus.unregister(this);
            isRegisteredWithBus = false;
        }


        if (navDrawer != null)
        {
            navDrawer.destroy();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId)
    {
        super.setContentView(layoutResId);

        toolbar = (Toolbar)findViewById(R.id.include_toolbar);

        //If we defined a toolbar, then set it as our action bar
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
        }
    }


    protected void setNavDrawer(NavDrawer navDrawer)
    {
        this.navDrawer = navDrawer;
        this.navDrawer.create();
    }

    public Toolbar getToolbar()
    {
        return toolbar;
    }

    public JaveShopApplication getJaveShopApplication()
    {
        return application;
    }
}
