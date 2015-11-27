package com.javeshop.javeshop.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.services.Product;
import com.javeshop.javeshop.services.Users;

/**
 * Dialogo que permite al usuario ingresar cuantas unidades de un producto hay disponibles o cuantas quiere comprar.
 */
public class QuantityDialog extends BaseDialogFragment
{
    public static final String MAX_QUANTITY = "MAX_QUANTITY";
    private NumberPicker numberPicker;
    private TextView text;
    private int maxQuantity;

    /**
     * Infla la itnerfaz del Dialog.
     * @param savedInstanceState
     * @return instancia del Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_quantity, null, false);

        text = (TextView) dialogView.findViewById(R.id.dialog_quantity_text);
        numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_quantity_numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setWrapSelectorWheel(true);

        Bundle args = getArguments();

        if (args != null)
        {
            numberPicker.setMaxValue(args.getInt(MAX_QUANTITY, 1));
        }

        String title = "Unidades";
        boolean unidades = true;

        if (numberPicker.getMaxValue() == 5)
        {
            title = "Calificación";
            unidades = false;
            numberPicker.setValue(5);
            text.setVisibility(View.VISIBLE);
            numberPicker.setWrapSelectorWheel(false);
        }

        final boolean finalUnidades = unidades;

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if (finalUnidades)
                        {
                            //bus.post(new Product.QuantityChanged(numberPicker.getValue()));


                            final Activity activity = getActivity();
                            AlertDialog dialog = new AlertDialog.Builder(activity)
                                    .setView(activity.getLayoutInflater().inflate(R.layout.dialog_payment_method, null, false))
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            RadioGroup group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.dialog_payment_method_radioGroup);

                                            if (group == null)
                                            {
                                                Toast.makeText(application, "Error. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                                                Log.e("QuantityDialog", "RadioGroup es null");
                                                return;
                                            }

                                            int id = group.getCheckedRadioButtonId();
                                            switch (id)
                                            {
                                                case R.id.dialog_payment_method_acordarVendedor:
                                                    bus.post(new Product.QuantityChanged(numberPicker.getValue()));
                                                    return;
                                                case R.id.dialog_payment_method_tarjetaCredito:
                                                    Log.e("Forma de pago", "Se scogio el metodo tarjeta de credito");
                                                    Activity act = activity;
                                                    if (act == null)
                                                    {
                                                        Log.e("Forma de pago", "getActivity() null");
                                                    }
                                                    AlertDialog dialogCreditCard = new AlertDialog.Builder(activity)
                                                            .setView(activity.getLayoutInflater().inflate(R.layout.dialog_credit_card_information, null, false))
                                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which)
                                                                {
                                                                    bus.post(new Product.QuantityChanged(numberPicker.getValue()));
                                                                }
                                                            })
                                                            .setNegativeButton("Cancelar", null)
                                                            .setTitle("Datos de tarjeta")
                                                            .show();
                                                    return;
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .setTitle("Forma de pago")
                                    .show();
                        }
                        else
                        {
                            bus.post(new Users.RateUserRequest(numberPicker.getValue()));
                        }

                    }
                })
                .setNegativeButton("Cancelar", null)
                .setTitle(title)
                .show();

        return dialog;
    }
}
