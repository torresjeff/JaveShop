package com.javeshop.javeshop.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ImagePagerAdapter;
import com.javeshop.javeshop.dialogs.QuantityDialog;
import com.javeshop.javeshop.infrastructure.User;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Esta Actividad muestra los detalles de un producto seleccionado.
 */
public class ProductDetailsActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    public static final String EXTRA_PRODUCT_DETAILS = "EXTRA_PRODUCT_DETAILS";

    private ProductDetails productDetails;

    private TextView name;
    private TextView state;
    private TextView price;
    private TextView vendor;
    private TextView description;
    private Button buyButton;

    private ImagePagerAdapter adapter;

    private Dialog progressDialog;
    private boolean progressBarVisible;
    private ViewPager viewPager;

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setTitle("Detalles");
        setNavDrawer(new MainNavDrawer(this));

        productDetails = getIntent().getParcelableExtra(EXTRA_PRODUCT_DETAILS);

        buyButton = (Button) findViewById(R.id.activity_product_details_buy);
        buyButton.setOnClickListener(this);
        if (productDetails.getQuantity() <= 0)
        {
            buyButton.setEnabled(false);
        }
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

        price.setText("$" + NumberFormat.getNumberInstance(Locale.US).format(productDetails.getPrice()));
        vendor.setText("Vendedor: " + productDetails.getOwnerFirstName() + " " + productDetails.getOwnerLastName());
        //Log.e("LiveProductService", "Response: firstName=" + productDetails.getOwnerFirstName() + ",lastName=" + productDetails.getOwnerLastName());
        description.setText(productDetails.getDescription());


        //Picasso.with(this).load(productDetails.getMainImageUrl()).into(currentImage);

        if (progressBarVisible)
        {
            setProgressBarVisible(true);
        }
    }

    /**
     * Muestra un Dialog que le da retroalimentacion al usuario para que sepa que se esta finalizando la transaccion.
     * @param newVisible
     */
    private void setProgressBarVisible(boolean newVisible)
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

    /**
     * Callback. Esta funcion se llama automaticamente luego de que el servidor responde afirmativa o negativamente al realizar la transaccion.
     * @param response respuesta del servidor.
     */
    @Subscribe
    public void onTransactionCompleted(Product.BuyProductResponse response)
    {
        setProgressBarVisible(false);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        startActivity(new Intent(this, BuyProductSuccessActivity.class));
        finish();
    }


    /**
     * Callback. Esta funcion se llama luego de que el servidor ha procesado la solicitud de marcar a un producto como favorito por parte de un usuario.
     * @param response respuesta del servidor.
     */
    @Subscribe
    public void onMarkedAsFavorite(Product.MarkAsFavoriteResponse response)
    {
        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        //TODO: cuando este marcado como favorito y se vuelva a press el boton, se debe eliminar de los favoritos.
        if (response.added)
        {
            Toast.makeText(this, "Agregado a tus favoritos", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Eliminado de tus favoritos", Toast.LENGTH_SHORT).show();
        }

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
            case R.id.activity_product_details_previousButton:
                previousPage();
                return;
            case R.id.activity_product_details_nextButton:
                nextPage();
                return;
            case R.id.activity_product_details_buy:
                buy();
                return;
            case R.id.activity_product_details_vendor:
                Intent intent = new Intent(this, UserDetailsActivity.class);
                intent.putExtra(UserDetailsActivity.EXTRA_USER_ID, productDetails.getOwnerId());
                startActivity(intent);
                return;
        }
    }

    /**
     * Esta funcion se encarga de empezar la transaccion, y enviar el request al servidor. El servidor determina si el usuario puede comprar o no dependiendo de su saldo.
     */
    private void buy()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack(null);
        QuantityDialog dialog = new QuantityDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(QuantityDialog.MAX_QUANTITY, productDetails.getQuantity());
        dialog.setArguments(bundle);
        dialog.show(transaction, null);
    }

    /**
     * Callback. Determina las unidades que va a comprar un usuario.
     * @param quantity unidades que el usuario va a comprar.
     */
    @Subscribe
    public void onQuantitySelected(Product.QuantityChanged quantity)
    {
        User user = application.getAuth().getUser();
        bus.post(new Product.BuyProductRequest(user.getId(), productDetails.getId(), quantity.value));
        setProgressBarVisible(true);
    }

    /**
     * Crea el menu de opciones
     * @param menu el menu que se va a inflar.
     * @return true si el menu fue creado satisfactoriamente.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_product_details, menu);
        return true;
    }

    /**
     * Responde a eventos del menu (click/touch en las opciones del menu)
     * @param item el item que fue clicked.
     * @return true si se respondio al evento.
     */
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

    /**
     * Cambia la pagina del ViewPager a la pagina siguiente para poder ver una otra imagen.
     */
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

    /**
     * Cambia la pagina del ViewPager a la pagina anterior para poder ver una otra imagen.
     */
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
