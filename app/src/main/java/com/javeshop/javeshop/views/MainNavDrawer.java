package com.javeshop.javeshop.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.activities.BaseActivity;
import com.javeshop.javeshop.activities.MainActivity;
import com.javeshop.javeshop.activities.ProfileActivity;
import com.javeshop.javeshop.activities.SellProductActivity;
import com.javeshop.javeshop.infrastructure.User;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class MainNavDrawer extends NavDrawer
{
    //The name of the user inside the nav drawer
    private final TextView displayName;
    //Avatar que se mustra en la parte de arriba del NavDrawer
    private final ImageView avatar;

    //Add all items (NavDrawerItems) of the NavDrawer here
    public MainNavDrawer(final BaseActivity activity)
    {
        super(activity);

        //Agregamos aqui los items que queramos que aparezcan en el NavDrawer

        addItem(new ActivityNavDrawerItem(MainActivity.class, "Búsqueda", 0, R.drawable.ic_search_black_24dp, R.id.include_main_nav_drawer_topItemsContainer));
        addItem(new ActivityNavDrawerItem(SellProductActivity.class, "Vender", 0, R.drawable.ic_attach_money_black_24dp, R.id.include_main_nav_drawer_topItemsContainer));
        addItem(new ActivityNavDrawerItem(ProfileActivity.class, "Mi cuenta", 0, R.drawable.ic_person_black_24dp, R.id.include_main_nav_drawer_topItemsContainer));

        //Items that don't start Activities should override onClick() so that they don't stay selected eternally.
        addItem(new BasicNavDrawerItem("Cerrar sesión", 0, R.drawable.ic_close_black_24dp, R.id.include_main_nav_drawer_bottomItemsContainer)
        {
            @Override
            public void onClick(View v)
            {
                activity.getJaveShopApplication().getAuth().logout();
            }
        });

        displayName = (TextView) activity.findViewById(R.id.include_main_nav_drawer_displayName);
        avatar = (ImageView) activity.findViewById(R.id.include_main_nav_drawer_avatar);

        User loggedInUser = activity.getJaveShopApplication().getAuth().getUser();


        displayName.setText(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

        Picasso.with(activity).load(loggedInUser.getAvatarUrl()).into(avatar);
    }

}
