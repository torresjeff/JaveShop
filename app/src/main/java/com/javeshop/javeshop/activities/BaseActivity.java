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
 * La Actividad Base para toda la aplicación. Todas las Actividades que componen JaveShop extienden de BaseActivity de manera directa o indirecta.
 * Esta Actividad se encarga de que todas las demas Actividades tengan la unica instancia de la aplicacion {@link com.javeshop.javeshop.infrastructure.JaveShopApplication}, que tengan acceso al bus de eventos, y que tengan la opcion de agregar un Toolbar y un NavDrawer.
 * Las Actividades que extienden a BaseActivity de manera directa no requieren que el usuario tenga una sesion inciada o que este registrado para poder interactuar con ellas.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private boolean isRegisteredWithBus;
    protected JaveShopApplication application;
    protected Toolbar toolbar;
    protected NavDrawer navDrawer;
    protected Bus bus;

    /**
     * Al momento que se crea esta Actividad, se encarga de guardar una referencia a JaveShopApplication, instanciar el bus de eventos y registrarse al mismo.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        application = (JaveShopApplication)getApplication();
        bus = application.getBus();

        bus.register(this);
        isRegisteredWithBus = true;
    }


    /**
     * Cuando termina esta Actividad, se elimina del bus de eventos para no seguir recibiendo eventos.
     */
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

    /**
     * Cuando se destruye esta Actividad, se elimina del bus de eventos para no seguir recibiendo eventos y ademas destruye el NavDrawer si existe.
     */
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

    /**
     * Infla la interfaz de la Actividad. Además se encarga de fijar el Toolbar en caso de que exista.
     * @param layoutResId el identificador unico de la interfaz.
     */
    @Override
    public void setContentView(@LayoutRes int layoutResId)
    {
        super.setContentView(layoutResId);

        toolbar = (Toolbar)findViewById(R.id.include_toolbar);

        //Si definimos un toolbar, lo usamos como ActionBar
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
