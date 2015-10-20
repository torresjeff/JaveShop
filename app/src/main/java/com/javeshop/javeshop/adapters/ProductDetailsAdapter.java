package com.javeshop.javeshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.activities.BaseActivity;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Adapter que permite mostrar los detalles de un producto en un ListView.
 */
public class ProductDetailsAdapter extends ArrayAdapter<ProductDetails>
{
    private LayoutInflater inflater;

    public ProductDetailsAdapter(BaseActivity activity)
    {
        super(activity, 0);
        inflater = activity.getLayoutInflater();
    }

    /**
     * Se encarga de instanciar los elementos de una fila para un ListView.
     * @param position posicion dentro de la lista.
     * @param convertView para reciclar una fila que esta fuera del rango.
     * @param parent el contenedor del elemento.
     * @return instancia un view para ser usado en cada fila individual del ListView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ProductDetails product = getItem(position);
        ViewHolder view;

        //We're not recycling a view, we have to instantiate a new item
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_item_product, parent, false);
            view = new ViewHolder(convertView);
            convertView.setTag(view);
        }
        else //recycle the view
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.name.setText(product.getName());


        if (product.getState() == 0)
        {
            view.state.setText("Nuevo");
        }
        else if (product.getState() == 1)
        {
            view.state.setText("Usado");
        }
        else
        {
            throw new RuntimeException("El estado del producto no esta definido (debe ser 0 o 1), estado = " + product.getState());
        }

        view.price.setText("$" + NumberFormat.getNumberInstance(Locale.US).format(product.getPrice()));


        Picasso.with(getContext()).load(product.getMainImageUrl()).into(view.image);

        return convertView;
    }

    /**
     * Contiene los elementos graficos que componen cada fila.
     */
    private class ViewHolder
    {
        public TextView name;
        public TextView state;
        public TextView price;
        public ImageView image;

        public ViewHolder(View view)
        {
            name = (TextView) view.findViewById(R.id.list_item_product_name);
            state = (TextView) view.findViewById(R.id.list_item_product_state);
            price = (TextView) view.findViewById(R.id.list_item_product_price);
            image = (ImageView) view.findViewById(R.id.list_item_product_image);
        }
    }
}
