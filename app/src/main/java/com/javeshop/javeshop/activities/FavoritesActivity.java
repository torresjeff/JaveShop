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
 * Created by Jeffrey Torres on 18/10/2015.
 */
public class FavoritesActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener
{
    private ProductDetailsAdapter adapter;
    private View progressBar;
    ListView listView;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_favorites);
        setNavDrawer(new MainNavDrawer(this));
        getSupportActionBar().setTitle("Mis favoritos");

        progressBar = findViewById(R.id.activity_favorites_progresFrame);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new ProductDetailsAdapter(this);

        listView = (ListView) findViewById(R.id.activity_favorites_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bus.post(new Product.GetFavoritesRequest(application.getAuth().getUser().getId()));
    }

    @Subscribe
    public void onFavoritesLoaded(Product.GetFavoritesResponse response)
    {
        progressBar.setVisibility(View.GONE);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        if (response.products.size() == 0)
        {
            listView.setEmptyView(findViewById(R.id.activity_favorites_emptyList));
            return;
        }

        adapter.addAll(response.products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ProductDetails productDetails = adapter.getItem(position);

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_DETAILS, productDetails);
        startActivity(intent);
    }
}
