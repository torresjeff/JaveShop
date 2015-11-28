package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.User;
import com.javeshop.javeshop.services.Account;
import com.javeshop.javeshop.services.Module;
import com.squareup.otto.Subscribe;

/**
 * Esta Actividad verifica que el usuario tenga su sesion iniciada, aun despues de haber cerrado la aplicacion.
 * Si si lo ha hecho continua a {@link com.javeshop.javeshop.activities.MainActivity}.
 * Si no lo ha hecho lo remite a {@link com.javeshop.javeshop.activities.LoginActivity}.
 */
public class AuthenticationActivity extends BaseActivity
{
    private Auth auth;
    public static final String EXTRA_RETURN_TO_ACTIVITY = "EXTRA_RETURN_TO_ACTIVITY";


    /**
     * Infla la interfaz de la Actividad.
     * @param savedInstanceState contiene datos guardados cuando la Actividad se recrea.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        //Si no tenemos un token tenemos que iniciar sesión otra vez
        if (!application.getAuth().hasAuthToken() || application.getAuth().getAuthToken() == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        auth = application.getAuth();

        bus.post(new Account.LoginWithLocalTokenRequest(auth.getAuthToken()));
    }


    /**
     * Callback. Se llama cuando el servidor responde si el usuario esta autenticado o no.
     * @param response respuesta del servidor.
     */
    @Subscribe
    public void onLoginWithLocalToken(Account.LoginWithLocalTokenResponse response)
    {
        //We did not successfully authenticated with our local token. We must log in again
        if (!response.succeeded())
        {

            //Toast.makeText(this, "Por favor intente nuevamente", Toast.LENGTH_SHORT).show();
            response.showErrorToast(this);
            auth.setAuthToken(null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        /*Intent intent;
        String returnTo = getIntent().getStringExtra(EXTRA_RETURN_TO_ACTIVITY);

        if (returnTo != null)
        {
            try
            {
                intent = new Intent(this, Class.forName(returnTo));
            }
            catch (Exception ignored)
            {
                intent = new Intent(this, MainActivity.class);
            }
        }
        else
        {
            intent = new Intent(this, MainActivity.class);
        }*/

        User user = auth.getUser();
        user.setLoggedIn(true);
        user.setAvatarUrl(response.avatarUrl);
        user.setReputation(response.reputation);
        user.setBalance(response.balance);
        user.setPhoneNumber(response.phoneNumber);
        user.setLastName(response.lastName);
        user.setFirstName(response.firstName);
        user.setEmail(response.email);
        user.setId(response.id);

        auth.setUser(user);
        //Module.updateApi();
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}
