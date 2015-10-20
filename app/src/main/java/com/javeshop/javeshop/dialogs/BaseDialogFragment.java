package com.javeshop.javeshop.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.squareup.otto.Bus;

/**
 * Fragment que se utilizara para mostrar dialogos al usuario. Todos los dialogos que se utilicen en la aplicacion deben extender esta clase.
 */
public class BaseDialogFragment extends DialogFragment
{
    protected JaveShopApplication application;
    protected Bus bus;

    /**
     * Infla la interfaz del Dialog y se registra con el bus de eventos.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        application = (JaveShopApplication) getActivity().getApplication();
        bus = application.getBus();

        bus.register(this);
    }


    /**
     * Se elimina del bus de eventos.
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        bus.unregister(this);
    }
}
