package com.javeshop.javeshop.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.services.Account;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class ChangePasswordDialog extends BaseDialogFragment implements View.OnClickListener
{
    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Dialog progressDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_password, null, false);

        currentPassword = (EditText) dialogView.findViewById(R.id.dialog_change_password_currentPassword);
        newPassword = (EditText) dialogView.findViewById(R.id.dialog_change_password_newPassword);
        confirmNewPassword = (EditText) dialogView.findViewById(R.id.dialog_change_password_confirmNewPassword);



        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Aceptar",null)
                .setNegativeButton("Cancelar", null)
                .setTitle("Cambiar contraseña")
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return dialog;
    }


    @Subscribe
    public void onPasswordUpdated(Account.ChangePasswordResponse response)
    {
        progressDialog.dismiss();
        progressDialog = null;

        if (response.succeeded())
        {
            Toast.makeText(getActivity(), "Contraseña actualizada", Toast.LENGTH_LONG).show();
            dismiss();
            return;
        }

        currentPassword.setError(response.getPropertyError("currentPassword"));
        newPassword.setError(response.getPropertyError("newPassword"));
        confirmNewPassword.setError(response.getPropertyError("confirmNewPassword"));

        response.showErrorToast(getActivity());
    }

    @Override
    public void onClick(View v)
    {
       progressDialog = new ProgressDialog.Builder(getActivity())
                                .setTitle("Cambiando contraseña")
                                .setCancelable(false)
                                .show();

        bus.post(new Account.ChangePasswordRequest(currentPassword.getText().toString(), newPassword.getText().toString(), confirmNewPassword.getText().toString()));
    }
}
