package com.javeshop.javeshop.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.javeshop.javeshop.R;
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
    private View progressFrame;


    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_user_details);

        int userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

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

        bus.post(new Users.GetUserDetailsRequest(userId));
        findViewById(R.id.activity_user_details_layout).setVisibility(View.GONE);
        progressFrame.setVisibility(View.VISIBLE);
    }

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
        reputation.setText(Float.toString(userDetails.getReputation()));

    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch(id)
        {
            case R.id.activity_user_details_report:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Se ha reportado a este usuario")
                        .setPositiveButton("Aceptar", null)
                        .create();

                dialog.show();
                return;
        }
    }
}
