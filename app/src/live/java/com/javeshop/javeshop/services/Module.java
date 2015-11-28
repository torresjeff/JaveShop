package com.javeshop.javeshop.services;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Calendar;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Encargado de instanciar las clases que van a manejar las solicitudes del usuario.
 */
public class Module
{
    //public static JaveShopWebService api;
    //public static JaveShopApplication app;

    /**
     * Instanciamos todas las clases que manejan las solicitudes del usuario
     * @param application instancia unica (Singleton) de nuestra aplicacion.
     */
    public static void register (JaveShopApplication application)
    {
        JaveShopWebService api = createWebService(application);
        //app = application;

        new LiveAccountService(api, application);
        new LiveProductService(api, application);
        new LiveUserService(api, application);
    }

    /**
     * Instancia el RestAdapter para manejar las solicitudes.
     * @param application instancia unica (Singleton) de nuestra aplicacion.
     * @return instancia del RestAdapter.
     */
    private static JaveShopWebService createWebService(JaveShopApplication application)
    {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new AuthInterceptor(application.getAuth()));

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(JaveShopApplication.API_ENDPOINT.toString())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(client))
                .build();

        return adapter.create(JaveShopWebService.class);
    }

    /**
     * Parte del pipeline de solicitudes OkHttp, nos da la oportunidad de mokdificar cualquier solicitud OkHttp que ocurra, en este caso para agregar la cabecera de Authroization (contiene el token del usuario).
     */
    private static class AuthInterceptor implements Interceptor
    {
        private final Auth auth;

        public AuthInterceptor(Auth auth)
        {
            this.auth = auth;
        }

        @Override
        public Response intercept(Chain chain) throws IOException
        {
            Request request = chain.request(); //Que cree la solicitud

            //Luego modificar la solicitud y darle nuestro token
            if (auth.hasAuthToken())
            {
                Log.e("Module", "We set the auth interceptor");
                request = request.newBuilder().addHeader("Authorization", auth.getAuthToken()).build(); //Recrear la solicitud con una nueva cabecera si tenemos un token.
            }

            Response response = chain.proceed(request);

            if (response.isSuccessful())
            {
                return response;
            }

            if (response.code() == 401 && auth.hasAuthToken())
            {
                auth.setAuthToken(null);
            }

            return response;
        }
    }
}
