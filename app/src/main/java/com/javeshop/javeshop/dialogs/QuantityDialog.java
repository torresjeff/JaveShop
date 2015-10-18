package com.javeshop.javeshop.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.services.Product;

/**
 * Created by Jeffrey Torres on 17/10/2015.
 */
public class QuantityDialog extends BaseDialogFragment
{
    private NumberPicker numberPicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_quantity, null, false);

        numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_quantity_numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setWrapSelectorWheel(true);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    //TODO: no funciona correctamente el click
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        bus.post(new Product.QuantityChanged(numberPicker.getValue()));
                    }
                })
                .setNegativeButton("Cancelar", null)
                .setTitle("Unidades disponibles")
                .show();

        return dialog;
    }
}
