package com.javeshop.javeshop.activities;

import android.os.Bundle;

import com.javeshop.javeshop.R;

/**
 * Actividad a la que se remite un usuario cuando ha olvidado su contraseña.
 */
public class ForgotPasswordActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
    }
}
