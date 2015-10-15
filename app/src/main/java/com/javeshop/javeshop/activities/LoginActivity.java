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
 * Created by Jeffrey Torres on 11/10/2015.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    private EditText username;
    private EditText password;
    private Button loginButton;
    private View progressBar;
    private String defaultLoginButtonText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.activity_login_email);
        password = (EditText) findViewById(R.id.activity_login_password);
        progressBar = findViewById(R.id.activity_login_progressBar);
        progressBar.setVisibility(View.GONE);
        loginButton = (Button) findViewById(R.id.activity_login_loginButton);
        defaultLoginButtonText = loginButton.getText().toString();
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch (id)
        {
            case R.id.activity_login_loginButton:
                login();
                return;
            case R.id.activity_login_registerButton:
                startActivity(new Intent(this, RegisterActivity.class));
                //TODO: implementar register button
                return;
            case R.id.activity_login_forgotPasswordButton:
                //TODO: implementar forgot password button
                return;
        }
    }

    private void login()
    {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setText("");
        loginButton.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);
        bus.post(new Account.LoginWithUsernameRequest(username.getText().toString(), password.getText().toString()));
    }


    @Subscribe
    public void onLoginWithUsername(Account.LoginWithUsernameResponse response)
    {
        if (response.succeeded())
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        response.showErrorToast(this);

        loginButton.setEnabled(true);

        username.setError(response.getPropertyError("userName"));
        username.setEnabled(true);

        password.setError(response.getPropertyError("password"));
        password.setEnabled(true);

        progressBar.setVisibility(View.GONE);
        loginButton.setText(defaultLoginButtonText);
    }
}
