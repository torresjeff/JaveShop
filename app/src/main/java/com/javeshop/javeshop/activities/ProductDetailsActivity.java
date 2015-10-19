package com.javeshop.javeshop.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ImagePagerAdapter;
import com.javeshop.javeshop.infrastructure.User;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

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

    private ImagePagerAdapter adapter;

    private Dialog progressDialog;
    private boolean progressBarVisible;
    private ViewPager viewPager;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setTitle("Detalles");
        setNavDrawer(new MainNavDrawer(this));

        productDetails = getIntent().getParcelableExtra(EXTRA_PRODUCT_DETAILS);

        findViewById(R.id.activity_product_details_buy).setOnClickListener(this);
        findViewById(R.id.activity_product_details_previousButton).setOnClickListener(this);
        findViewById(R.id.activity_product_details_nextButton).setOnClickListener(this);

        name = (TextView) findViewById(R.id.activity_product_details_name);
        state = (TextView) findViewById(R.id.activity_product_details_state);
        price = (TextView) findViewById(R.id.activity_product_details_price);
        description = (TextView) findViewById(R.id.activity_product_details_description);
        vendor = (TextView) findViewById(R.id.activity_product_details_vendor);
        vendor.setPaintFlags(vendor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        vendor.setOnClickListener(this);

        adapter = new ImagePagerAdapter(this);

        viewPager = (ViewPager) findViewById(R.id.activity_product_details_pager);
        viewPager.setAdapter(adapter);
        adapter.addAll(productDetails.getProductImagesUrls());
        adapter.notifyDataSetChanged();



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

        //Picasso.with(this).load(productDetails.getMainImageUrl()).into(currentImage);

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

    @Subscribe
    public void onMarkedAsFavorite(Product.MarkAsFavoriteResponse response)
    {
        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        //TODO: cuando este marcado como favorito y se vuelva a press el boton, se debe eliminar de los favoritos.
        Toast.makeText(this, "Agregado a tus favoritos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.activity_product_details_previousButton:
                previousPage();
                return;
            case R.id.activity_product_details_nextButton:
                nextPage();
                return;
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
            case R.id.activity_product_details_vendor:
                Intent intent = new Intent(this, UserDetailsActivity.class);
                intent.putExtra(UserDetailsActivity.EXTRA_USER_ID, productDetails.getOwnerId());

                startActivity(intent);
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_product_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.activity_product_details_menu_favorite:
                //TODO: post MarkAsFavoriteRequest
                //Toast.makeText(this, "Marcado como favorito", Toast.LENGTH_SHORT).show();
                bus.post(new Product.MarkAsFavoriteRequest(application.getAuth().getUser().getId(), productDetails.getId()));
                return true;
            case R.id.activity_product_details_menu_comments:
                //TODO: start ProductCommentsActivity, post GetProductCommentsRequest desde esa Activity
                Intent intent = new Intent(this, ProductCommentsActivity.class);
                intent.putExtra(EXTRA_PRODUCT_DETAILS, productDetails);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void nextPage()
    {
        if (adapter.getCount() == 0)
        {
            return;
        }

        int lastIndex = viewPager.getCurrentItem();

        if (lastIndex >= adapter.getCount() - 1)
        {
            viewPager.setCurrentItem(0, false);
        }
        else
        {
            viewPager.setCurrentItem(lastIndex + 1);
        }
    }

    private void previousPage()
    {
        if (adapter.getCount() == 0)
        {
            return;
        }

        int firstIndex = viewPager.getCurrentItem();

        if (firstIndex <= 0)
        {
            viewPager.setCurrentItem(adapter.getCount() - 1, false);
        }
        else
        {
            viewPager.setCurrentItem(firstIndex - 1);
        }
    }
}
