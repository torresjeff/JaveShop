package com.javeshop.javeshop.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.dialogs.QuantityDialog;
import com.javeshop.javeshop.services.Users;
import com.javeshop.javeshop.services.entities.UserDetails;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

/**
 * Created by Jeffrey Torres on 18/10/2015.
 */
public class UserDetailsActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private ImageView avatar;
    private TextView completeName;
    private TextView reputation;
    private Button reportButton;
    private Button rateButton;
    private View progressFrame;
    private int userId;

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_user_details);

        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

        if (userId == -1)
        {
            throw new IllegalArgumentException("No existe un usuario con ID -1");
        }

        progressFrame = findViewById(R.id.activity_user_details_progressFrame);
        avatar = (ImageView) findViewById(R.id.activity_user_details_avatar);
        completeName = (TextView) findViewById(R.id.activity_user_details_name);
        reputation = (TextView) findViewById(R.id.activity_user_details_reputation);
        reportButton = (Button) findViewById(R.id.activity_user_details_report);
        reportButton.setOnClickListener(this);
        rateButton = (Button) findViewById(R.id.activity_user_details_rate);
        rateButton.setOnClickListener(this);

        bus.post(new Users.GetUserDetailsRequest(userId));
        findViewById(R.id.activity_user_details_layout).setVisibility(View.GONE);
        progressFrame.setVisibility(View.VISIBLE);
    }

    /**
     * Callback. Se llama automaticamente cunado el servidor responde con la informacion del usuario.
     * @param response respuesta del servidor.
     */
    @Subscribe
    public void onUserDetailsLoaded(Users.GetUserDetailsResponse response)
    {
        findViewById(R.id.activity_user_details_layout).setVisibility(View.VISIBLE);
        progressFrame.setVisibility(View.GONE);
        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        UserDetails userDetails = response.userDetails;

        Picasso.with(this).load(userDetails.getAvatarUrl()).into(avatar);
        completeName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
        reputation.setText(/*Float.toString(userDetails.getReputation())*/String.format("%.1f", userDetails.getReputation()));

    }

    @Subscribe
    public void onUserRated(Users.RateUserResponse response)
    {
        if (!response.succeeded())
        {
            response.showErrorToast(this);
            return;
        }

        Toast.makeText(this, "Hemos registrado tu calificación", Toast.LENGTH_SHORT).show();
    }

    /**
     * Responde a eventos de clicks/touch.
     * @param view el View que fue tocado.
     */
    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch(id)
        {
            case R.id.activity_user_details_report:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Se ha reportado a este usuario")
                        .setPositiveButton("Aceptar", null)
                        .create();

                dialog.show();
                return;
            case R.id.activity_user_details_rate:
                FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack(null);
                QuantityDialog dialogRating = new QuantityDialog();
                Bundle args = new Bundle();
                args.putInt(QuantityDialog.MAX_QUANTITY, 5);
                args.putInt(QuantityDialog.USER_ID, userId);
                dialogRating.setArguments(args);
                dialogRating.show(transaction, null);
                return;
        }
    }
}
