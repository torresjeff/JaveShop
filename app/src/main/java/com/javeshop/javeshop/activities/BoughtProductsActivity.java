package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ProductDetailsAdapter;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.squareup.otto.Subscribe;

/**
 * Actividad que permite ver los productos que un usuario ha comprado.
 */
public class BoughtProductsActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener
{
    private ProductDetailsAdapter adapter;
    private View progressBar;
    private ListView listView;

    /**
     * Infla la interfaz de la Actividad.
     * @param savedInstanceState contiene datos guardados cuando la Actividad se recrea.
     */
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_bought_products);
        setNavDrawer(new MainNavDrawer(this));
        getSupportActionBar().setTitle("Mis compras");

        progressBar = findViewById(R.id.activity_bought_products_progresFrame);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new ProductDetailsAdapter(this);

        listView = (ListView) findViewById(R.id.activity_bought_products_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bus.post(new Product.GetBoughtProductsRequest());
    }

    /**
     * Callback. Esta funcion es llamada automaticamente cuando el servidor ha respondido con los productos que el usuario ha comprado.
     * @param response respuesta del servidor con los datos relevantes.
     */
    @Subscribe
    public void onBoughtProductsLoaded(Product.GetBoughtProductsResponse response)
    {
        progressBar.setVisibility(View.GONE);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        if (response.products.size() == 0)
        {
            listView.setEmptyView(findViewById(R.id.activity_bought_products_emptyList));
            return;
        }

        adapter.clear();
        adapter.addAll(response.products);
        adapter.notifyDataSetChanged();
    }

    /**
     * Maneja clicks en un producto.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ProductDetails productDetails = adapter.getItem(position);

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_DETAILS, productDetails);
        startActivity(intent);
    }
}
