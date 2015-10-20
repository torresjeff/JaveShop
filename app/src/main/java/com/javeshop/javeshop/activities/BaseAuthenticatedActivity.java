package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Se encarga que todas las clases que extiendan a BaseAuthenticatedActivity, obliguen a que el usuario tenga sesion iniciada.
 */
public abstract class BaseAuthenticatedActivity extends BaseActivity
{
    /**
     * Infla la interfaz de la Actividad.
     * @param savedInstanceState
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (!application.getAuth().getUser().isLoggedIn())
        {
            //If we HAVE a login token, then try automatically logging in with our local token
            if (application.getAuth().hasAuthToken())
            {
                Intent intent = new Intent(this, AuthenticationActivity.class); //AuthenticationActivity is jut an Activity that tries to relog us in automatically
                //intent.putExtra(AuthenticationActivity.EXTRA_RETURN_TO_ACTIVITY, /*getClass().getName()*/MainActivity.class);
                startActivity(intent);
            }
            else //if we don't have a login token, then we must login again
            {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
            return;
        }

        //If we are logged in then we implement this method. This makes sure this code doesn't run if we aren't logged in.
        onJaveShopCreate(savedInstanceState);
    }

    /**
     * Todas las clases que extiendan a BaseAuthenticatedActivity no van a llamar onCreate(Bundle), sino que deben llamar onJaveShopCreate(Bundle) para inflar su interfaz.
     * @param savedInstanceState
     */
    protected abstract void onJaveShopCreate(Bundle savedInstanceState);
}
