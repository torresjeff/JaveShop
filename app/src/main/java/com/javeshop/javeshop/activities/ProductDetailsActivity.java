package com.javeshop.javeshop.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.infrastructure.User;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

/**
 * Created by Jeffrey Torres on 14/10/2015.
 */
public class ProductDetailsActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    public static final String EXTRA_PRODUCT_DETAILS = "EXTRA_PRODUCT_DETAILS";

    ProductDetails productDetails;

    private TextView name;
    private TextView state;
    private TextView price;
    private TextView vendor;
    private TextView description;
    private ImageView currentImage;

    private Dialog progressDialog;
    private boolean progressBarVisible;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setTitle("Detalles");
        setNavDrawer(new MainNavDrawer(this));

        productDetails = getIntent().getParcelableExtra(EXTRA_PRODUCT_DETAILS);

        findViewById(R.id.activity_product_details_buy).setOnClickListener(this);
        findViewById(R.id.activity_product_details_seeComments).setOnClickListener(this);

        name = (TextView) findViewById(R.id.activity_product_details_name);
        state = (TextView) findViewById(R.id.activity_product_details_state);
        price = (TextView) findViewById(R.id.activity_product_details_price);
        vendor = (TextView) findViewById(R.id.activity_product_details_vendor);
        description = (TextView) findViewById(R.id.activity_product_details_description);
        currentImage = (ImageView) findViewById(R.id.activity_product_details_currentImage);

        //TODO: poner un view pager para ver las imagenes del producto

        name.setText(productDetails.getName());

        if (productDetails.getState() == 0)
        {
            state.setText("Nuevo");
        }
        else if (productDetails.getState() == 1)
        {
            state.setText("Usado");
        }
        else
        {
            throw new RuntimeException("El estado del producto no esta definido (debe ser 0 o 1), estado = " + productDetails.getState());
        }

        price.setText("$" + productDetails.getPrice());
        vendor.setText("Vendedor " + productDetails.getOwnerId());
        description.setText(productDetails.getDescription());

        //TODO: poner el nombre del vendedor del producto. Traer el id del vendedor cuando se selecciona un producto?

        Picasso.with(this).load(productDetails.getMainImageUrl()).into(currentImage);

        if (progressBarVisible)
        {
            setProgressBarVisible(true);
        }
    }

    public void setProgressBarVisible(boolean newVisible)
    {
        if (newVisible)
        {
            progressDialog = new ProgressDialog.Builder(this)
                    .setTitle("Finalizando transacción")
                    .setCancelable(false)
                    .show();
        }
        else if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }


        this.progressBarVisible = newVisible;
    }

    @Subscribe
    public void onTransactionCompleted(Product.BuyProductResponse response)
    {
        setProgressBarVisible(false);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.activity_product_details_buy:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("¿Estás seguro que deseas comprar este producto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                User user = application.getAuth().getUser();
                                bus.post(new Product.BuyProductRequest(user.getId(), productDetails.getId()));
                                setProgressBarVisible(true);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();

                dialog.show();
                return;
            case R.id.activity_product_details_seeComments:
                //TODO: start ProductCommentsActivity, pasar el id del producto
                return;
        }
    }
}
