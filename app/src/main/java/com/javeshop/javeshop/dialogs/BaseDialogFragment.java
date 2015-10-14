package com.javeshop.javeshop.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.squareup.otto.Bus;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class BaseDialogFragment extends DialogFragment
{
    protected JaveShopApplication application;
    protected Bus bus;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        application = (JaveShopApplication) getActivity().getApplication();
        bus = application.getBus();

        bus.register(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        bus.unregister(this);
    }
}
