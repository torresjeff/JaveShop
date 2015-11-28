package com.javeshop.javeshop.infrastructure;


import android.app.Application;
import android.net.Uri;

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

    //TODO: cambiar al servidor de verdad
    public static final Uri API_ENDPOINT = Uri.parse("http://javeshop.me");
    //public static final Uri API_ENDPOINT = Uri.parse("http://10.0.3.2/JaveShop");

    /**
     * Constructor.
     */
    public JaveShopApplication()
    {
        bus = new Bus();
    }

    /**
     * Crea una nueva instancia de Auth y se registra con Module para instanciar la interfaz que manejar las solicitudes.
     */
    @Override
    public void onCreate()
    {
        super.onCreate();

        auth = new Auth(this);

        Module.register(this);
    }

    /**
     * Get de Auth.
     * @return Auth
     */
    public Auth getAuth()
    {
        return auth;
    }

    /**
     * Get de Bus.
     * @return Bus.
     */
    public Bus getBus()
    {
        return bus;
    }
}
