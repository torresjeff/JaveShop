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
 * Created by Jeffrey Torres on 19/10/2015.
 */
public class PostedProductsActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener
{
    private ProductDetailsAdapter adapter;
    private View progressBar;
    ListView listView;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_posted_products);
        setNavDrawer(new MainNavDrawer(this));
        getSupportActionBar().setTitle("Mis productos");

        progressBar = findViewById(R.id.activity_posted_products_progresFrame);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new ProductDetailsAdapter(this);

        listView = (ListView) findViewById(R.id.activity_posted_products_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bus.post(new Product.GetPostedProductsRequest(application.getAuth().getUser().getId()));
    }

    @Subscribe
    public void onPostedProductsLoaded(Product.GetPostedProductsResponse response)
    {
        progressBar.setVisibility(View.GONE);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        if (response.products.size() == 0)
        {
            listView.setEmptyView(findViewById(R.id.activity_posted_products_emptyList));
            return;
        }

        adapter.addAll(response.products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ProductDetails productDetails = adapter.getItem(position);

        Intent intent = new Intent(this, SellProductActivity.class);
        intent.putExtra(SellProductActivity.EXTRA_PRODUCT_DETAILS, productDetails);
        intent.putExtra(SellProductActivity.EXTRA_IS_EDITING, true);
        startActivity(intent);
    }
}
