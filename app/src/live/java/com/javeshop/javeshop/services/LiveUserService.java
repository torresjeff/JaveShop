package com.javeshop.javeshop.services;

import android.util.Log;

import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.infrastructure.RetrofitCallbackPost;
import com.squareup.otto.Subscribe;

/**
 * Se encarga de manejar las solicitudes que tienen que ver con los usuarios.
 */
public class LiveUserService extends BaseLiveService
{
    private final Auth auth;

    /**
     * Constructor
     * @param api interfaz por medio de la cual vamos a enviar los mensajes al servidor.
     * @param application instancia unica (Singleton) de nuestra aplicacion.
     */
    public LiveUserService(JaveShopWebService api, JaveShopApplication application)
    {
        super(api, application);
        auth = application.getAuth();
    }

    /**
     * Escucha el evento para cuando un usuario quiere ver los datos publics de un vendedor (nombre, reputacion, avatar) y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void searchUser(Users.GetUserDetailsRequest request)
    {
        api.searchUser(request.userId, new RetrofitCallbackPost<Users.GetUserDetailsResponse>(Users.GetUserDetailsResponse.class, bus)
        {
            @Override
            protected void onResponse(Users.GetUserDetailsResponse response)
            {
                bus.post(response);
            }
        });
    }

    /**
     * Escucha el evento para cuando un usuario quiere calificar a un vendedor y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void rateUser(Users.RateUserRequest request)
    {
        Log.e("LiveUserService", "Recibio RateUserRequest");
        api.rateUser(request, new RetrofitCallbackPost<Users.RateUserResponse>(Users.RateUserResponse.class, bus)
        {
            @Override
            protected void onResponse(Users.RateUserResponse response)
            {
                bus.post(response);
            }
        });
    }
}
