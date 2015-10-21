package com.javeshop.javeshop.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.javeshop.javeshop.R;

/**
 * Se instancia luego de que un usuario se ha resgistrado satisfactoriamente en la aplicacion.
 */
public class RegisterSuccessActivity extends BaseActivity implements View.OnClickListener
{
    private Button button;

    /**
     * Infla la interfaz de la Actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_success);

        button = (Button) findViewById(R.id.activity_register_success_back);
        button.setOnClickListener(this);
    }

    /**
     * Responde a eventos de clicks/touch.
     * @param view el View que fue tocado.
     */
    @Override
    public void onClick(View view)
    {
        finish();
    }
}
