package com.javeshop.javeshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.javeshop.javeshop.R;

/**
 * Created by Jeffrey Torres on 17/10/2015.
 */
public class PostProductSuccessActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    private Button button;
    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_post_product_success);

        button = (Button) findViewById(R.id.activity_post_product_succes_continue);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        switch (id)
        {
            case R.id.activity_post_product_succes_continue:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
        }
    }
}
