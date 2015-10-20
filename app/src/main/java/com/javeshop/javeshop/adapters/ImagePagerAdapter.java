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
 * Adapter que permite mostrar de manera correcta la imagen de un producto en un ViewPager.
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


    /**
     * Infla un view (una imagen) para agregar al ViewPager.
     * @param container el contenedor de la imagen
     * @param position posicion en el ViewPager.
     * @return View instanciado.
     */
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

        imageView.setImageResource(0);
        Picasso.with(context).load(Uri.parse(stringArray[position])).into(imageView);

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

    /**
     * Agrega una nueva pagina al ViewPager.
     * @param resource direccion (URL) de la imagen que se va a agregar.
     */
    public void add(String resource)
    {
        imageResources.add(resource);
    }

    /**
     * Agrega varias paginas al ViewPager.
     * @param resources direcciones (URL) de las imagenes que se van a agregar.
     */
    public void addAll(ArrayList<String> resources)
    {
        imageResources.addAll(resources);
    }

    /**
     * Eliminar una pagina del ViewPager.
     * @param container contenedor de la pagina.
     * @param position posicion en el ViewPager.
     * @param object la instancia de la pagina.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout) object);
    }
}
