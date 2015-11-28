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
 * Esta Actividad se encarga de mostrarle a un usuario todos los productos que ha marcado como favoritos.
 */
public class FavoritesActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener
{
    private ProductDetailsAdapter adapter;
    private View progressBar;
    private ListView listView;

    /**
     * Infla la interfaz de la Actividad.
     * @param savedInstanceState
     */
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

    /**
     * Callback. Esta funcion es llamada automaticamente cuando el servidor ha respondido con los favoritos del usuario.
     * @param response respuesta del servidor con los datos relevantes.
     */
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

        adapter.clear();
        adapter.addAll(response.products);
        adapter.notifyDataSetChanged();
    }

    /**
     * Responde a eventos clicks/touch en la lista de productos.
     * @param parent el contenedor de la lista.
     * @param view el View que fue clicked.
     * @param position la posicion en la lista del elemento que fue clicked.
     * @param id id del elemento que fue clicked.
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
