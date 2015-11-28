package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.services.Account;
import com.squareup.otto.Subscribe;

/**
 * Esta Actividad se instancia cuando el usuario no tiene una cuenta activa y desea registrarse en JaveShop.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener
{
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText confirmPassword;
    private EditText phoneNumber;
    private View progressBar;
    private Button registerButton;

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.activity_register_email);
        firstName = (EditText) findViewById(R.id.activity_register_firstName);
        lastName = (EditText) findViewById(R.id.activity_register_lastName);
        password = (EditText) findViewById(R.id.activity_register_password);
        confirmPassword = (EditText) findViewById(R.id.activity_register_confirmPassword);
        phoneNumber = (EditText) findViewById(R.id.activity_register_phoneNumber);
        progressBar = findViewById(R.id.activity_register_progressBar);
        progressBar.setVisibility(View.GONE);
        registerButton = (Button) findViewById(R.id.activity_register_registerButton);
        registerButton.setOnClickListener(this);
    }

    /**
     * Responde a eventos de clicks/touch.
     * @param view el View que fue tocado.
     */
    @Override
    public void onClick(View view)
    {
        if (firstName.getText().toString().isEmpty())
        {
            firstName.setError("Por favor ingresa tu nombre");
            return;
        }

        if (lastName.getText().toString().isEmpty())
        {
            lastName.setError("Por favor ingresa tu apellido");
            return;
        }

        if (lastName.getText().toString().isEmpty())
        {
            lastName.setError("Por favor ingresa tu número de teléfono");
            return;
        }

        if (password.getText().toString().isEmpty())
        {
            password.setError("Ingresa una contraseña");
            return;
        }


        if (!password.getText().toString().equals(confirmPassword.getText().toString()))
        {
            confirmPassword.setError("Las contraseñas no coinciden");
            return;
        }

        //if (!email.getText().toString().matches("^[_a-zA-Z\\+]+(\\.[_a-zA-Z]+)*@javeriana+(\\.edu+)*(\\.co)$"))
        if (!email.getText().toString().toUpperCase().contains("@JAVERIANA.EDU.CO"))
        {
            email.setError("Intenta de nuevo. Debes tener un correo electrónico de la Javeriana.");
            return;
        }

        bus.post(new Account.RegisterRequest(firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                phoneNumber.getText().toString()));
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);
    }

    /**
     * Callback. Se llama automaticamente cuando el servidor responde al request de registrarse.
     * @param response
     */
    @Subscribe
    public void onRegistered(Account.RegisterResponse response)
    {
        progressBar.setVisibility(View.GONE);
        registerButton.setEnabled(true);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        startActivity(new Intent(this, RegisterSuccessActivity.class));
        finish();
    }
}
