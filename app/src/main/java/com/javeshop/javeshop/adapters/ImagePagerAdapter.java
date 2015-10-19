package com.javeshop.javeshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.activities.BaseActivity;
import com.javeshop.javeshop.activities.ProductImageActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jeffrey Torres on 15/10/2015.
 */
public class ImagePagerAdapter extends PagerAdapter
{
    private static final String TAG = "ImagePagerAdapter";
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> imageResources;

    public ImagePagerAdapter(BaseActivity activity)
    {
        context = activity;
        inflater = activity.getLayoutInflater();
        imageResources = new ArrayList<>();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        View itemView = inflater.inflate(R.layout.pager_item_image, container, false);

        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, ProductImageActivity.class);
                intent.putExtra(ProductImageActivity.EXTRA_IMAGE, imageResources.get(position));
                context.startActivity(intent);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                //TODO: contextual menu, para poder borrar la imagen. De pronto implementando llamando una funcion en el padre por medio de una interfaz/callback?
                return true;
            }
        });

        ImageView imageView = (ImageView) itemView.findViewById(R.id.pager_item_imageView);

        String[] stringArray = imageResources.toArray(new String[]{});


        Log.e(TAG, "Attempting to load " + stringArray[position]);
        //TODO: picasso no sirve con local images cuando se utiliza resize. No muestra la imagen en el viewpager
        imageView.setImageResource(0);
        Picasso.with(context).load(Uri.parse(stringArray[position]))/*.resize(200, 200).centerCrop()*/.into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount()
    {
        return imageResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    public void add(String resource)
    {
        imageResources.add(resource);
    }

    public void addAll(ArrayList<String> resources)
    {
        imageResources.addAll(resources);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout) object);
    }
}
