package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.javeshop.javeshop.R;

/**
 * Esta Actividad es instanciada despues de que un usuario ha comprado un producto satisfactoriamente.
 */
public class BuyProductSuccessActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    private Button button;
    private TextView textView;

    /**
     * Infla la inerfaz de la Actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_post_product_success);

        textView = (TextView) findViewById(R.id.activity_post_product_success_description);

        textView.setText("Te hemos enviado un correo electrónico con los datos del vendedor para que se puedan poner en contacto.\n¡Gracias por confiar en JaveShop para hacer tus compras!");

        button = (Button) findViewById(R.id.activity_post_product_succes_continue);
        button.setText("Sigue comprando");

        button.setOnClickListener(this);
    }

    /**
     * Responde a eventos de clicks/touch.
     * @param view el View que fue tocado.
     */
    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch (id)
        {
            case R.id.activity_post_product_succes_continue:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
        }
    }
}
