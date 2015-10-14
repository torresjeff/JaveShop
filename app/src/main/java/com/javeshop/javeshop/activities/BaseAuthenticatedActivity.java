package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Jeffrey Torres on 11/10/2015.
 */
public abstract class BaseAuthenticatedActivity extends BaseActivity
{
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
                intent.putExtra(AuthenticationActivity.EXTRA_RETURN_TO_ACTIVITY, getClass().getName());
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

    protected abstract void onJaveShopCreate(Bundle savedInstanceState);
}
