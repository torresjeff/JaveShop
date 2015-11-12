package com.javeshop.javeshop.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.javeshop.javeshop.activities.LoginActivity;

/**
 * Esta clase tiene los componentes necesario para autenticar a un usuario.
 */
public class Auth
{
    private static final String AUTH_PREFERENCES = "AUTH_PREFERENCES";
    private static final String AUTH_PREFERENCES_TOKEN = "AUTH_PREFERENCES_TOKEN";


    private final Context context;
    private final SharedPreferences preferences;

    private User user;
    private String authToken;


    public Auth(Context context)
    {
        this.context = context;
        user = new User();

        //SharedPreferences persists after the application has been closed
        preferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        authToken = preferences.getString(AUTH_PREFERENCES_TOKEN, null); //null = default value
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public boolean hasAuthToken()
    {
        return (authToken != null && !authToken.isEmpty());
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AUTH_PREFERENCES_TOKEN, this.authToken);
        editor.commit();
    }

    /**
     * Borra el token del usuario que tenia sesion iniciada para que no siga iniciando sesion automaticamente la proxima vez que abra la aplicacion.
     */
    public void logout()
    {
        setAuthToken(null);
        user.setLoggedIn(false);

        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(loginIntent);
    }
}
