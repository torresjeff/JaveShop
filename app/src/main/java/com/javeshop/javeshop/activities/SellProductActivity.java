package com.javeshop.javeshop.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ImagePagerAdapter;
import com.javeshop.javeshop.dialogs.ChangePasswordDialog;
import com.javeshop.javeshop.dialogs.QuantityDialog;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.javeshop.javeshop.services.entities.ProductDetailsSell;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.soundcloud.android.crop.Crop;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeffrey Torres on 15/10/2015.
 */
public class SellProductActivity extends BaseActivity implements View.OnClickListener
{
    private static final int REQUEST_SELECT_IMAGE = 100;

    private static final String BUNDLE_OUTPUT_FILE_EXTRA = "BUNDLE_OUTPUT_FILE_EXTRA";
    private static final String BUNDLE_IMAGES_EXTRA = "BUNDLE_IMAGES_EXTRA";
    private static final String BUNDLE_QUANTITY_EXTRA = "BUNDLE_QUANTITY_EXTRA";

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

    private int quantity;
    //private boolean isFileCreated;


    @Override
    //protected void onJaveShopCreate(Bundle savedInstanceState)
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);

        setNavDrawer(new MainNavDrawer(this));
        getSupportActionBar().setTitle("Vender");

        tempOutputFiles = new ArrayList<>();
        outputFiles = new ArrayList<>();

        findViewById(R.id.activity_sell_product_takePictureButton).setOnClickListener(this);
        findViewById(R.id.activity_sell_product_post).setOnClickListener(this);
        findViewById(R.id.activity_sell_product_nextButton).setOnClickListener(this);
        findViewById(R.id.activity_sell_product_previousButton).setOnClickListener(this);

        name = (EditText) findViewById(R.id.activity_sell_product_name);
        price = (EditText) findViewById(R.id.activity_sell_product_price);
        description = (EditText) findViewById(R.id.activity_sell_product_description);

        //TODO: agregar campo de categoria
        //TODO: agregar campo de descripción, borrar SellProductPartTwoActivity, ya estamos usando scrollview.

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
        /*else
        {
            findViewById(R.id.activity_sell_product_pagerContainer).setVisibility(View.VISIBLE);
        }*/

        Log.e("SellProductActivity", "onCreate called");

    }

    @Subscribe
    public void onProductPosted(Product.PostProductResponse response)
    {
        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        startActivity(new Intent(this, PostProductSuccessActivity.class));
        finish();
    }

    @Subscribe
    public void onQuantityChanged(Product.QuantityChanged quantity)
    {
        quantityButton.setText(Integer.toString(quantity.value));
        this.quantity = quantity.value;
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch (id)
        {
            case R.id.activity_sell_product_previousButton:
                Log.e("SellProduct", "currentItem = " + viewPager.getCurrentItem());
                previousPage();

                return;
            case R.id.activity_sell_product_nextButton:
                Log.e("SellProduct", "currentItem = " + viewPager.getCurrentItem());
                nextPage();

                return;
            case R.id.activity_sell_product_takePictureButton:
                addPicture();
                return;

            case R.id.activity_sell_product_post:
                //TODO: mandar request de post product
                //Verificar que la persona no haya dejado "Categoría" seleccionada en el spinner
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
                        stateSpinner.getSelectedItemPosition());

                bus.post(new Product.PostProductRequest(productDetails));
                return;
            case R.id.activity_sell_product_quantityButton:
                FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack(null);
                QuantityDialog dialog = new QuantityDialog();
                dialog.show(transaction, null);
                return;
        }
    }

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

            findViewById(R.id.activity_sell_product_pagerContainer).setVisibility(View.VISIBLE);


            viewPager.setCurrentItem(adapter.getCount() - 1);
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putSerializable(BUNDLE_OUTPUT_FILE_EXTRA, (Serializable) tempOutputFiles);
        outState.putStringArrayList(BUNDLE_IMAGES_EXTRA, outputFiles);
        outState.putString(BUNDLE_QUANTITY_EXTRA, quantityButton.getText().toString());
    }
}
