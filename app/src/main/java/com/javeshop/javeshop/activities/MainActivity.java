package com.javeshop.javeshop.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 11/10/2015.
 */
public class MainActivity extends BaseAuthenticatedActivity
{
    private View progressFrame;
    private SearchView searchView;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_main);

        setNavDrawer(new MainNavDrawer(this));

        progressFrame = findViewById(R.id.activity_main_progresFrame);
        progressFrame.setVisibility(View.GONE);

        searchView = new SearchView(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(searchView);

        searchView.setIconified(false); //El searchView empieza expandido. Cuando setIconified es false, se muestra un icono
        searchView.setQueryHint("Búsqueda JaveShop");
        searchView.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //TODO: send SearchProductRequest to server, que responda con productos quemados en el codigo
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }
}
