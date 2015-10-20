package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.javeshop.javeshop.R;

/**
 * Created by Jeffrey Torres on 19/10/2015.
 */
public class BuyProductSuccessActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    private Button button;
    private TextView textView;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_post_product_success);

        textView = (TextView) findViewById(R.id.activity_post_product_success_description);

        textView.setText("Se te ha enviado un correo electrónico con los datos del vendedor para que se puedan poner en contacto.\n¡Gracias por confiar en JaveShop para hacer tus compras!");

        button = (Button) findViewById(R.id.activity_post_product_succes_continue);

        button.setOnClickListener(this);
    }

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
