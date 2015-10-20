package com.javeshop.javeshop.infrastructure;


import android.app.Application;

import com.javeshop.javeshop.services.Module;
import com.squareup.otto.Bus;

/**
 * Instancia unica (Singleton) de la clase Application que requiere de manera obligatoria cada aplicacion.
 * En esta clase guardamos todos los elementos que sabemos solo seben instanciar una vez (por ejemplo el bus de eventos), y que el resto de Actividades puedan a acceder a ellos.
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
