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
 * Created by Jeffrey Torres on 1/11/2015.
 */
public class Module
{
    //public static JaveShopWebService api;
    //public static JaveShopApplication app;
    public static void register (JaveShopApplication application)
    {
        JaveShopWebService api = createWebService(application);
        //app = application;

        new LiveAccountService(api, application);
        new LiveProductService(api, application);
        new LiveUserService(api, application);
    }

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

    /*public static void updateApi()
    {
        Log.e("MODULE", "updateApi called");
        api = createWebService(app);

    }*/

    /**
     * Part of the OkHttp request pipeline, and it's gonna give the opportunity to modify any OkHttp requests that happens, adding the auth token header
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
            Request request = chain.request(); //Continue its own way to create the request for us

            //Then modify the request and give it our AuthToken
            if (auth.hasAuthToken())
            {
                Log.e("Module", "We set the auth interceptor");
                request = request.newBuilder().addHeader("Authorization", /*"Bearer " + */auth.getAuthToken()).build(); //Recreate the request with a new header if we have an auth token
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
