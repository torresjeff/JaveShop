package com.javeshop.javeshop.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ProductDetailsAdapter;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 11/10/2015.
 */
public class MainActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener
{
    private View progressFrame;
    private SearchView searchView;
    private ProductDetailsAdapter adapter;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_main);

        setNavDrawer(new MainNavDrawer(this));

        progressFrame = findViewById(R.id.activity_main_progresFrame);
        progressFrame.setVisibility(View.GONE);

        searchView = new SearchView(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(searchView);

        searchView.setIconified(false); //El SearchView empieza expandido. Cuando setIconified es false, se muestra un icono en vez de la barra de texto.
        searchView.setQueryHint("Búsqueda JaveShop");
        searchView.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT));
        searchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //TODO: mandar SearchProductRequest al server
                progressFrame.setVisibility(View.VISIBLE);
                bus.post(new Product.SearchProductRequest(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        adapter = new ProductDetailsAdapter(this);

        ListView listView = (ListView) findViewById(R.id.activity_main_productsListView);
        listView.setEmptyView(findViewById(R.id.activity_main_emptyList));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        ProductDetails productDetails = adapter.getItem(position);

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_DETAILS, productDetails);
        startActivity(intent);
    }

    @Subscribe
    public void onProductSearched(Product.SearchProductResponse response)
    {
        progressFrame.setVisibility(View.GONE);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        adapter.clear();
        adapter.addAll(response.products);
        adapter.notifyDataSetChanged();
    }


}
