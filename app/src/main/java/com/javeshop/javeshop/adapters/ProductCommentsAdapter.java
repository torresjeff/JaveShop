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
 * Created by Jeffrey Torres on 18/10/2015.
 */
public class ProductCommentsAdapter extends ArrayAdapter<ProductComment>
{
    private LayoutInflater inflater;

    public ProductCommentsAdapter(BaseActivity activity)
    {
        super(activity, 0);
        inflater = activity.getLayoutInflater();
    }

    //Returns an instantiated view that will be used for each individual row in the listView
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


        if (comment.getReply() == null || comment.getReply().isEmpty())
        {
            view.reply.setText("Aún no han respondido esta pregunta.");
        }
        else
        {
            view.reply.setText(comment.getReply());
        }

        return convertView;
    }

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
