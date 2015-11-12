package com.javeshop.javeshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.activities.BaseActivity;
import com.javeshop.javeshop.services.entities.ProductComment;

/**
 * Adapter que permite mostrar los comentarios de un producto en un ListView.
 */
public class ProductCommentsAdapter extends ArrayAdapter<ProductComment>
{
    private LayoutInflater inflater;

    public ProductCommentsAdapter(BaseActivity activity)
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
        ProductComment comment = getItem(position);
        ViewHolder view;

        //We're not recycling a view, we have to instantiate a new item
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_item_comment, parent, false);
            view = new ViewHolder(convertView);
            convertView.setTag(view);
        }
        else //recycle the view
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.question.setText(comment.getQuestion());


        if (comment.getReply() == null || comment.getReply().isEmpty() || comment.getReply().equalsIgnoreCase("NULL"))
        {
            view.reply.setText("Aún no han respondido esta pregunta.");
        }
        else
        {
            view.reply.setText(comment.getReply());
        }

        return convertView;
    }

    /**
     * Contiene los elementos graficos que componen cada fila.
     */
    private class ViewHolder
    {
        public TextView question;
        public TextView reply;

        public ViewHolder(View view)
        {
            question = (TextView) view.findViewById(R.id.list_item_comment_question);
            reply = (TextView) view.findViewById(R.id.list_item_comment_reply);
        }
    }
}
