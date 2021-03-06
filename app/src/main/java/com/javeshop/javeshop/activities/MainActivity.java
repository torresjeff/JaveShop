package com.javeshop.javeshop.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ProductDetailsAdapter;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Esta es la Actividad principal de la aplicacion. En ella podemos buscar algun producto por nombre.
 */
public class MainActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener
{
    private static final String BUNDLE_PRODUCT_LIST = "BUNDLE_PRODUCT_LIST";

    private View progressFrame;
    private SearchView searchView;
    private ProductDetailsAdapter adapter;
    private String lastQuery;
    private ArrayList<ProductDetails> productDetails;

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_main);

        Log.e("MAINACTIVITY", application.getAuth().getUser().toString());

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
                progressFrame.setVisibility(View.VISIBLE);
                lastQuery = query;
                bus.post(new Product.SearchProductRequest(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        productDetails = new ArrayList<>();
        adapter = new ProductDetailsAdapter(this);

        ListView listView = (ListView) findViewById(R.id.activity_main_productsListView);
        listView.setEmptyView(findViewById(R.id.activity_main_emptyList));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        if (savedInstanceState != null)
        {
            productDetails = savedInstanceState.getParcelableArrayList(BUNDLE_PRODUCT_LIST);
            adapter.addAll(productDetails);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Responde a eventos clicks/touch en la lista de productos.
     * @param adapterView el contenedor de la lista.
     * @param view el View que fue clicked.
     * @param position la posicion en la lista del elemento que fue clicked.
     * @param l id del elemento que fue clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        ProductDetails productDetails = adapter.getItem(position);

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_DETAILS, productDetails);
        startActivity(intent);
    }

    /**
     * Callback. Es llamada cuando el servidor responde con la lista de productos que corresponden con la busqueda del usuario.
     * @param response
     */
    @Subscribe
    public void onProductSearched(Product.SearchProductResponse response)
    {
        if (!response.query.equals(lastQuery))
        {
            return;
        }

        progressFrame.setVisibility(View.GONE);

        if (!response.succeeded())
        {
            //TODO: el toast de cuando no encuentra ningun producto se muestra dos veces
            response.showErrorToast(this);
            return;
        }

        if (response.products.size() == 0)
        {
            ((TextView)findViewById(R.id.activity_main_emptyList)).setText("No se encontró ningún producto");
            productDetails.clear();
            adapter.clear();
            adapter.notifyDataSetChanged();
            return ;
        }

        productDetails.clear();
        productDetails.addAll(response.products);
        adapter.clear();
        adapter.addAll(productDetails);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList(BUNDLE_PRODUCT_LIST, productDetails);
    }
}
