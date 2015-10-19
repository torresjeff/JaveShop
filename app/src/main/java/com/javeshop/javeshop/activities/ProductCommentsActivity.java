package com.javeshop.javeshop.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ProductCommentsAdapter;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductComment;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 18/10/2015.
 */
public class ProductCommentsActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    private int productId;
    private View progressBar;
    private EditText commentText;
    private Button postButton;

    private ProductCommentsAdapter adapter;
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_product_comments);

        ProductDetails productDetails = getIntent().getParcelableExtra(ProductDetailsActivity.EXTRA_PRODUCT_DETAILS);

        progressBar = findViewById(R.id.activity_product_comments_progressBar);
        commentText = (EditText) findViewById(R.id.activity_product_commentText);

        postButton = (Button) findViewById(R.id.activity_product_comments_post);
        postButton.setOnClickListener(this);

        adapter = new ProductCommentsAdapter(this);

        ListView listView = (ListView) findViewById(R.id.activity_product_comments_listView);
        listView.setAdapter(adapter);
        listView.setEnabled(false);

        bus.post(new Product.GetProductCommentsRequest(productDetails.getId()));
        productId = productDetails.getId();

        progressBar.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onCommentsLoaded(Product.GetProductCommentsResponse response)
    {
        progressBar.setVisibility(View.GONE);
        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        adapter.addAll(response.comments);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onCommentPosted(Product.SendCommentResponse response)
    {
        progressBar.setVisibility(View.GONE);
        postButton.setEnabled(true);

        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        commentText.setText(null);

        adapter.add(response.comment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            case R.id.activity_product_comments_post:
                if (commentText.getText().toString().isEmpty())
                {
                    commentText.setError("Por favor escribe una pregunta primero");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                postButton.setEnabled(false);
                ProductComment productComment = new ProductComment(productId, application.getAuth().getUser().getId(), commentText.getText().toString());
                bus.post(new Product.SendCommentRequest(productComment));
                return;
        }
    }
}
