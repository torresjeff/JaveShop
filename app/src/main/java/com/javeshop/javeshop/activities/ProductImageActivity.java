package com.javeshop.javeshop.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.javeshop.javeshop.R;
import com.squareup.picasso.Picasso;

/**
 * Esta Actividad muestra en pantalla completa la imagen de un producto.
 */
public class ProductImageActivity extends BaseActivity
{
    public static final String EXTRA_IMAGE = "EXTRA_IMAGE";

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_image);

        //TODO: cuando venga del servidor se debe quitar el Uri.parse, para que cargue directamente desde el link de la imagen
        //Picasso.with(this).load(Uri.parse(getIntent().getStringExtra(EXTRA_IMAGE))).into((ImageView) findViewById(R.id.activity_product_image_image));
        Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE)).into((ImageView) findViewById(R.id.activity_product_image_image));
    }
}
