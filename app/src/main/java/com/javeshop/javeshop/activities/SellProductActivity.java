package com.javeshop.javeshop.activities;

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
import android.widget.Spinner;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ImagePagerAdapter;
import com.javeshop.javeshop.services.entities.ProductDetailsSell;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.soundcloud.android.crop.Crop;

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

    private Spinner spinner;
    private ProductDetailsSell productDetails;
    private ArrayList<File> tempOutputFiles;
    private ArrayList<String> outputFiles;
    private ImagePagerAdapter adapter;
    private ViewPager viewPager;
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

        //TODO: agregar campo de categoria
        //TODO: agregar campo de descripción, borrar SellProductPartTwoActivity, ya estamos usando scrollview.

        spinner = (Spinner) findViewById(R.id.activity_sell_product_stateSpinner);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Nuevo", "Usado"}));

        adapter = new ImagePagerAdapter(this);

        viewPager = (ViewPager) findViewById(R.id.activity_sell_product_pager);
        viewPager.setAdapter(adapter);


        if (savedInstanceState != null)
        {
            tempOutputFiles = (ArrayList<File>)savedInstanceState.getSerializable(BUNDLE_OUTPUT_FILE_EXTRA);
            outputFiles = savedInstanceState.getStringArrayList(BUNDLE_IMAGES_EXTRA);
            adapter.addAll(outputFiles);
            adapter.notifyDataSetChanged();
        }

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

            new Crop(outputFile)
                    .asSquare()
                    .output(tempFileUri)
                    .start(this);
        }

        else if (requestCode == Crop.REQUEST_CROP)
        {
            outputFiles.add(Uri.fromFile(tempOutputFiles.get(tempOutputFiles.size() - 1)).toString());
            //outputFiles.add(tempOutputFiles.get(tempOutputFiles.size() - 1).toString());
            Log.e("SellProductActivity", "Added new image to adapter: " + outputFiles.get(outputFiles.size() - 1));
            adapter.add(outputFiles.get(outputFiles.size() - 1));
            adapter.notifyDataSetChanged();
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
    }
}
