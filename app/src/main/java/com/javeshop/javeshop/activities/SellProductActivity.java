package com.javeshop.javeshop.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ImagePagerAdapter;
import com.javeshop.javeshop.dialogs.QuantityDialog;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.soundcloud.android.crop.Crop;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Esta Actividad le presenta al usuario los campos de informacion que debe llenar para vender un producto.
 */
public class SellProductActivity extends BaseActivity implements View.OnClickListener
{
    private static final int REQUEST_SELECT_IMAGE = 100;

    private static final String BUNDLE_OUTPUT_FILE_EXTRA = "BUNDLE_OUTPUT_FILE_EXTRA";
    private static final String BUNDLE_IMAGES_EXTRA = "BUNDLE_IMAGES_EXTRA";
    private static final String BUNDLE_QUANTITY_EXTRA = "BUNDLE_QUANTITY_EXTRA";

    public static final String EXTRA_IS_EDITING = "EXTRA_IS_EDITING";
    public static final String EXTRA_PRODUCT_DETAILS = "EXTRA_PRODUCT_DETAILS";

    private EditText name;
    private EditText price;
    private Spinner stateSpinner;
    private Spinner categorySpinner;
    private Button quantityButton;
    private EditText description;
    private ArrayList<File> tempOutputFiles;
    private ArrayList<String> outputFiles;
    private ImagePagerAdapter adapter;
    private ViewPager viewPager;
    private ProgressBar progressBar;
    private Button postButton;

    private int quantity;
    private boolean isEditing;
    private ProductDetails postedProductDetails;
    private Dialog progressDialog;
    private boolean progressBarVisible;


    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);

        setNavDrawer(new MainNavDrawer(this));
        getSupportActionBar().setTitle("Vender");

        tempOutputFiles = new ArrayList<>();
        outputFiles = new ArrayList<>();

        findViewById(R.id.activity_sell_product_takePictureButton).setOnClickListener(this);
        postButton = (Button) findViewById(R.id.activity_sell_product_post);
        postButton.setOnClickListener(this);
        findViewById(R.id.activity_sell_product_nextButton).setOnClickListener(this);
        findViewById(R.id.activity_sell_product_previousButton).setOnClickListener(this);

        name = (EditText) findViewById(R.id.activity_sell_product_name);
        price = (EditText) findViewById(R.id.activity_sell_product_price);
        description = (EditText) findViewById(R.id.activity_sell_product_description);

        progressBar = (ProgressBar) findViewById(R.id.activity_sell_product_progressBar);
        //TODO: agregar campo de categoria

        stateSpinner = (Spinner) findViewById(R.id.activity_sell_product_stateSpinner);
        stateSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Nuevo", "Usado"}));
        categorySpinner = (Spinner) findViewById(R.id.activity_sell_product_categorySpinner);
        categorySpinner.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        new String[]{"Categoría", "Carros, motos", "Inmuebles", "Servicios", "Accesorios para vehículos", "Bebés", "Cámaras y accesorios",
                        "Celulares y teléfonos", "Coleccionables y hobbies", "Computación", "Consolas y videojuegos", "Deportes y fitness", "Electrodomésticos",
                        "Electrónica, audio y video", "Hogar y muebles", "Industrias y oficinas", "Instrumentos musicales", "Juegos y juguetes", "Libros, revistas y comics",
                        "Música, películas y series", "Relojes y joyas", "Ropa y accesorios", "Salud y belleza", "Otra categoría"}));

        quantityButton = (Button) findViewById(R.id.activity_sell_product_quantityButton);
        quantityButton.setOnClickListener(this);

        adapter = new ImagePagerAdapter(this);

        viewPager = (ViewPager) findViewById(R.id.activity_sell_product_pager);
        viewPager.setAdapter(adapter);


        Intent data = getIntent();

        isEditing = data.getBooleanExtra(EXTRA_IS_EDITING, false);

        if (isEditing)
        {
            postedProductDetails = data.getParcelableExtra(EXTRA_PRODUCT_DETAILS);
            postButton.setText("Actualizar");
            getSupportActionBar().setTitle("Modificar datos");

            if (postedProductDetails != null)
            {
                name.setText(postedProductDetails.getName());
                price.setText(Float.toString(postedProductDetails.getPrice()));
                stateSpinner.setSelection(postedProductDetails.getState());
                quantityButton.setText(Integer.toString(postedProductDetails.getQuantity()));
                categorySpinner.setSelection(postedProductDetails.getCategory());

                adapter.addAll(postedProductDetails.getProductImagesUrls());
                adapter.notifyDataSetChanged();

                description.setText(postedProductDetails.getDescription());
            }
        }

        quantity = 1;

        if (savedInstanceState != null)
        {
            tempOutputFiles = (ArrayList<File>)savedInstanceState.getSerializable(BUNDLE_OUTPUT_FILE_EXTRA);
            outputFiles = savedInstanceState.getStringArrayList(BUNDLE_IMAGES_EXTRA);
            adapter.addAll(outputFiles);
            adapter.notifyDataSetChanged();

            String howMany = savedInstanceState.getString(BUNDLE_QUANTITY_EXTRA);
            quantityButton.setText(howMany);
            quantity = Integer.parseInt(howMany);
        }

        if (adapter.getCount() == 0)
        {
            findViewById(R.id.activity_sell_product_pagerContainer).setVisibility(View.GONE);
        }

        if (progressBarVisible)
        {
            setProgressBarVisible(true);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        invalidateOptionsMenu();
    }

    /**
     * Callback. Se llama automaticamente cuando el servidor responde si el producto fue publicado correctamente.
     * @param response respuesta del servidor.
     */
    @Subscribe
    public void onProductPosted(Product.PostProductResponse response)
    {
        progressBar.setVisibility(View.GONE);
        postButton.setEnabled(true);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        startActivity(new Intent(this, PostProductSuccessActivity.class));
        finish();
    }

    /**
     * Callback. Se llama automaticamente cuando se actualizan los datos de un producto que ya fue publicado.
     * @param response respuestsa del servidor
     */
    @Subscribe
    public void onProductUpdated(Product.UpdateProductDetailsResponse response)
    {
        progressBar.setVisibility(View.GONE);
        postButton.setEnabled(true);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        Toast.makeText(this, "Se han actualizado los datos del prodcuto", Toast.LENGTH_SHORT).show();
    }


    /**
     * Cambia la cantidad de unidades disponibles del producto.
     * @param quantity nueva cantidad de unidades.
     */
    @Subscribe
    public void onQuantityChanged(Product.QuantityChanged quantity)
    {
        quantityButton.setText(Integer.toString(quantity.value));
        this.quantity = quantity.value;
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
            case R.id.activity_sell_product_previousButton:
                previousPage();
                return;
            case R.id.activity_sell_product_nextButton:
                nextPage();
                return;
            case R.id.activity_sell_product_takePictureButton:
                addPicture();
                return;

            case R.id.activity_sell_product_post:
                if (name.getText().toString().isEmpty())
                {
                    name.setError("Ingresa el nombre del producto");
                    return;
                }

                if (price.getText().toString().isEmpty())
                {
                    price.setError("Ingresa el precio del producto");
                    return;
                }

                if (description.getText().toString().isEmpty())
                {
                    description.setError("Ingresa una descripción del producto");
                    return;
                }

                if (categorySpinner.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(this, "Por favor selecciona una categoria", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProductDetails productDetails = new ProductDetails(
                        application.getAuth().getUser().getId(),
                        name.getText().toString(),
                        description.getText().toString(),
                        outputFiles,
                        Float.parseFloat(price.getText().toString()),
                        quantity,
                        stateSpinner.getSelectedItemPosition(),
                        categorySpinner.getSelectedItemPosition());

                if (isEditing)
                {
                    //TODO: deberia post postedProductDetails (porque ya tiene un ID asignado) con un PUT request.
                    bus.post(new Product.UpdateProductDetailsRequest(productDetails));
                }
                else
                {
                    bus.post(new Product.PostProductRequest(productDetails));
                }

                progressBar.setVisibility(View.VISIBLE);
                postButton.setEnabled(false);
                return;
            case R.id.activity_sell_product_quantityButton:
                FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack(null);
                QuantityDialog dialog = new QuantityDialog();
                dialog.show(transaction, null);
                return;
        }
    }

    /**
     * Agrega una nueva imagen al producto.
     */
    private void addPicture()
    {
        List<Intent> otherImageCaptureIntents = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities = getPackageManager().queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);

        tempOutputFiles.add(new File(getExternalCacheDir(), "javeshop_product_image_" + tempOutputFiles.size() + ".jpg"));
        Log.e("SellProductActivity", "Added new tempOutputFile, tempOutputFiles.size() = " + tempOutputFiles.size());
        Log.e("SellProductActivity", "tempOutputFile name = " + Uri.fromFile(tempOutputFiles.get(tempOutputFiles.size() - 1)));

        for (ResolveInfo info : otherImageCaptureActivities)
        {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutputFiles.get(tempOutputFiles.size() - 1)));
            otherImageCaptureIntents.add(captureIntent);
        }

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser = Intent.createChooser(selectImageIntent, "Escoge tu imagen");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, otherImageCaptureIntents.toArray(new Parcelable[otherImageCaptureIntents.size()]));

        startActivityForResult(chooser, REQUEST_SELECT_IMAGE);
    }


    /**
     * Llamada cuando se escoge alguna imagen.
     * @param requestCode la solicitud que se hizo (seleccionar una imagen o crop).
     * @param resultCode si fue exitoso o no.
     * @param data datos adicionales (por ejemplo la imagen misma).
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            tempOutputFiles.get(tempOutputFiles.size() - 1).delete();
            tempOutputFiles.remove(tempOutputFiles.size() - 1);
            //outputFiles.remove(outputFiles.size() - 1);
            Log.e("SellProductActivity", "resultCode != RESULT_OK, tempOutputFiles.size() = " + tempOutputFiles.size());
            Log.e("SellProductActivity", "resultCode != RESULT_OK, outputFiles.size() = " + outputFiles.size());
            return;
        }

        if (requestCode == REQUEST_SELECT_IMAGE)
        {
            Uri outputFile;
            int lastIndex = tempOutputFiles.size() - 1;

            Log.e("SellProductActivity", "REQUEST_SELECT_IMAGE, tempOutputFiles.size() = " + tempOutputFiles.size());

            Uri tempFileUri = Uri.fromFile(tempOutputFiles.get(lastIndex));

            if (data != null && (data.getAction() == null || !data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE)))
            {
                outputFile = data.getData();
            }

            else
            {
                outputFile = tempFileUri;
            }

            Crop.of(outputFile, tempFileUri).asSquare().start(this);
            /*new Crop(outputFile)
                    .asSquare()
                    .output(tempFileUri)
                    .start(this);*/
        }

        else if (requestCode == Crop.REQUEST_CROP)
        {
            outputFiles.add(Uri.fromFile(tempOutputFiles.get(tempOutputFiles.size() - 1)).toString());
            Log.e("SellProductActivity", "Added new image to adapter: " + outputFiles.get(outputFiles.size() - 1));
            adapter.add(outputFiles.get(outputFiles.size() - 1));
            adapter.notifyDataSetChanged();
            //Para forzar que vuelva a cargar la imagen y que no utilice el cache. Porque la imagen se llama igual pero el contenido cambio.
            Picasso.with(this).invalidate(Uri.fromFile(tempOutputFiles.get(tempOutputFiles.size() - 1)).toString());
            //Picasso.with(this).cache.clear();
            findViewById(R.id.activity_sell_product_pagerContainer).setVisibility(View.VISIBLE);


            viewPager.setCurrentItem(adapter.getCount() - 1);
        }
    }

    /**
     * Infla el menu de opciones de la Actividad.
     * @param menu el menu que se va a inflar.
     * @return verdadero si fue creado satisfactoriamente.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (isEditing)
        {
            getMenuInflater().inflate(R.menu.activity_sell_product, menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Responde a eventos de click/touch en el menu.
     * @param item elemento del menu que fue seleccionado.
     * @return true si se manejo el evento correctamente.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.activity_sell_product_menu_delete:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("¿Estás seguro que deseas eliminar este producto?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                bus.post(new Product.DeleteProductRequest(postedProductDetails.getId()));
                                setProgressBarVisible(true);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create();

                dialog.show();
                return true;
            case R.id.activity_sell_product_menu_comments:
                Intent intent = new Intent(this, ProductCommentsActivity.class);
                intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_DETAILS, postedProductDetails);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setProgressBarVisible(boolean newVisible)
    {
        if (newVisible)
        {
            progressDialog = new ProgressDialog.Builder(SellProductActivity.this)
                    .setTitle("Eliminando producto")
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
     * Callback. Llamado cuando el servidor responde al request de eliminar un producto.
     * @param response respuesta del servidor.
     */
    @Subscribe
    public void onProductDeleted(Product.DeleteProductResponse response)
    {
        setProgressBarVisible(false);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        finish();
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

    /**
     * Guarda la informacion que tiene la Actividad cuando se va a recrear.
     * @param outState objeto donde se guarda la infomracion necesaria.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putSerializable(BUNDLE_OUTPUT_FILE_EXTRA, (Serializable) tempOutputFiles);
        outState.putStringArrayList(BUNDLE_IMAGES_EXTRA, outputFiles);
        outState.putString(BUNDLE_QUANTITY_EXTRA, quantityButton.getText().toString());
    }
}
