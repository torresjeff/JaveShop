package com.javeshop.javeshop.services;

import android.os.Handler;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.squareup.otto.Bus;

import java.util.Random;

/**
 * Clase que contiene las funciones basicas para que se puedan emular las respuestas del servidor.
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

    /**
     * Llama una funcion luego de un retardo aleatorio entre millisecondMin y millisecondMax.
     * @param runnable funcion que se va a llamar.
     * @param millisecondMin tiempo minimo de espera.
     * @param millisecondMax tiempo maximo de espera.
     */
    protected void invokeDelayed(Runnable runnable, long millisecondMin, long millisecondMax)
    {
        if (millisecondMin > millisecondMax)
        {
            throw new IllegalArgumentException("Min debe ser mas pequeno que max");
        }

        long delay = (long)(random.nextDouble() * (millisecondMax - millisecondMin)) + millisecondMin;
        handler.postDelayed(runnable, delay);
    }

    /**
     * Publica un evento al bus de eventos luego de un retardo aleatorio entre millisecondMin y millisecondMax.
     * @param event evento que se va a publicar.
     * @param millisecondMin tiempo minimo de espera.
     * @param millisecondMax tiempo maximo de espera.
     */
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

    /**
     * Publica un evento al bus de eventos luego de un retardo especificado por milliseconds.
     * @param event evento que se va a publicar.
     * @param milliseconds tiempo de espera.
     */
    protected void postDelayed(Object event, long milliseconds)
    {
        postDelayed(event, milliseconds, milliseconds);
    }

    /**
     * Publica un evento en un tiempo de espera entre 600 y 1200 milisegundos.
     * @param event evento que se va a publicar.
     */
    protected void postDelayed(Object event)
    {
        postDelayed(event, 600, 1200);
    }
}
