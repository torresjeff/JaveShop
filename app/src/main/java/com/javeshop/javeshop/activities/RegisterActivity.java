package com.javeshop.javeshop.activities;

import android.os.Bundle;
import android.widget.EditText;

import com.javeshop.javeshop.R;

/**
 * Esta Actividad se instancia cuando el usuario no tiene una cuenta activa y desea registrarse en JaveShop.
 */
public class RegisterActivity extends BaseActivity
{
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText confirmPassword;
    private EditText phoneNumber;

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        //TODO: implementar RegisterActivity
    }
}
