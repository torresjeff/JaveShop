package com.javeshop.javeshop.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.adapters.ProductCommentsAdapter;
import com.javeshop.javeshop.adapters.ProductDetailsAdapter;
import com.javeshop.javeshop.dialogs.QuantityDialog;
import com.javeshop.javeshop.dialogs.ReplyCommentDialog;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductComment;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.squareup.otto.Subscribe;

/**
 * Esta Actividad muestra los comentarios de un producto en especifico.
 */
public class ProductCommentsActivity extends BaseAuthenticatedActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private int productId;
    private View progressBar;
    private EditText commentText;
    private Button postButton;

    private ProductCommentsAdapter adapter;
    private ListView listView;
    private ProductDetails productDetails;

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_product_comments);

        productDetails = getIntent().getParcelableExtra(ProductDetailsActivity.EXTRA_PRODUCT_DETAILS);

        progressBar = findViewById(R.id.activity_product_comments_progressBar);
        commentText = (EditText) findViewById(R.id.activity_product_commentText);

        postButton = (Button) findViewById(R.id.activity_product_comments_post);
        postButton.setOnClickListener(this);

        adapter = new ProductCommentsAdapter(this);

        listView = (ListView) findViewById(R.id.activity_product_comments_listView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        bus.post(new Product.GetProductCommentsRequest(productDetails.getId()));
        productId = productDetails.getId();

        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Callback. Esta funcion se llama automaticamente cuando el servidor ha respondido con todos los comentarios del producto.
     * @param response
     */
    @Subscribe
    public void onCommentsLoaded(Product.GetProductCommentsResponse response)
    {
        progressBar.setVisibility(View.GONE);
        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        adapter.clear();
        adapter.addAll(response.comments);
        adapter.notifyDataSetChanged();

        if (adapter.getCount() == 0)
        {
            listView.setEmptyView(findViewById(R.id.activity_product_comments_emptyList));
        }
    }

    /**
     * Callback. Esta funcion se llama automaticamente cuando el servidor ha respondido, luego de que el usuario ha hecho un comentario. Se llama para reflejar el cambio en la interfaz, agregando el nuevo comentario a la lista.
     * @param response
     */
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

    @Subscribe
    public void onReplyPosted(Product.ReplyPostedEvent event)
    {
        bus.post(new Product.GetProductCommentsRequest(productDetails.getId()));

        progressBar.setVisibility(View.VISIBLE);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (productDetails.getOwnerId() == application.getAuth().getUser().getId() && (adapter.getItem(position).getReply() == null || adapter.getItem(position).getReply().equalsIgnoreCase("NULL")))
        {
            FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack(null);
            ReplyCommentDialog dialog = new ReplyCommentDialog();
            Bundle bundle = new Bundle();
            //bundle.putInt(ReplyCommentDialog.PRODUCT_ID, productDetails.getId());
            bundle.putInt(ReplyCommentDialog.PRODUCT_ID, /*((ProductDetailsAdapter)view)*/adapter.getItem(position).getCommentId());
            bundle.putInt(ReplyCommentDialog.REPLIER_ID, application.getAuth().getUser().getId());
            dialog.setArguments(bundle);
            dialog.show(transaction, null);
        }
    }
}
