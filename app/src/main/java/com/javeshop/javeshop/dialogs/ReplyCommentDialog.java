package com.javeshop.javeshop.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.services.Account;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.entities.ProductComment;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 12/11/2015.
 */
public class ReplyCommentDialog extends BaseDialogFragment implements View.OnClickListener
{
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String REPLIER_ID = "REPLIER_ID";

    private EditText reply;
    private Dialog progressDialog;
    private int productId, replierId;

    /**
     * Infla la itnerfaz del Dialog.
     * @param savedInstanceState
     * @return instancia del Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_reply_comment, null, false);

        Bundle args = getArguments();

        if (args != null)
        {
            productId = args.getInt(PRODUCT_ID, 0);
            replierId = args.getInt(REPLIER_ID, 0);
        }

        reply = (EditText) dialogView.findViewById(R.id.dialog_reply_comment_comment);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Responder",null)
                .setNegativeButton("Cancelar", null)
                .setTitle("Responder comentario")
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return dialog;
    }

    /**
     * Callback. Se llama automaticamente cuando el servidor responde si el comentario fue publicado o no.
     * @param response respuesta del servidor.
     */
    @Subscribe
    public void onReplyPosted(Product.ReplyCommentResponse response)
    {
        progressDialog.dismiss();
        progressDialog = null;

        if (response.succeeded())
        {
            Toast.makeText(getActivity(), "Comentario publicado", Toast.LENGTH_LONG).show();
            bus.post(new Product.ReplyPostedEvent());;
            dismiss();
            return;
        }

        response.showErrorToast(getActivity());
    }

    /**
     * Responde a eventos de clicks/touch.
     * @param view el View que fue tocado.
     */
    @Override
    public void onClick(View view)
    {
        progressDialog = new ProgressDialog.Builder(getActivity())
                .setTitle("Publicando comentario")
                .setCancelable(false)
                .show();


        bus.post(new Product.ReplyCommentRequest(productId, replierId, reply.getText().toString()));
    }
}
