package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.javeshop.javeshop.R;

/**
 * Esta Actividad es instanciada despues de que un usuario ha publicado un producto satisfactoriamente.
 */
public class PostProductSuccessActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    private Button button;

    /**
     * Infla la interfaz de la Actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_post_product_success);

        button = (Button) findViewById(R.id.activity_post_product_succes_continue);

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
