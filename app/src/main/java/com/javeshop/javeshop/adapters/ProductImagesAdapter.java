package com.javeshop.javeshop.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.activities.BaseActivity;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.squareup.picasso.Picasso;

/**
 * Created by Jeffrey Torres on 15/10/2015.
 */
public class ProductImagesAdapter extends ArrayAdapter<Uri>
{
    private LayoutInflater inflater;

    public ProductImagesAdapter(BaseActivity activity)
    {
        super(activity, 0);
        inflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //TODO: cambiar de Uri a link (String) cuando se utilice el servidor
        Uri image = getItem(position);
        ViewHolder view;

        //We're not recycling a view, we have to instantiate a new item
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_item_product_image, parent, false);
            view = new ViewHolder(convertView);
            convertView.setTag(view);
        }
        else //recycle the view
        {
            view = (ViewHolder) convertView.getTag();
        }

        //TODO: cambiar de Uri a link (String) cuando se utilice el servidor
        Picasso.with(getContext()).load(image).into(view.image);

        return convertView;
    }

    private class ViewHolder
    {
        public ImageView image;

        public ViewHolder(View view)
        {
            image = (ImageView) view.findViewById(R.id.list_item_product_image_image);
        }
    }
}
