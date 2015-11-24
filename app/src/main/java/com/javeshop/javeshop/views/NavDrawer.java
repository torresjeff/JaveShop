package com.javeshop.javeshop.views;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.activities.BaseActivity;

import java.util.ArrayList;

/**
 * Representa un NavDrawer (menu deslizante) que pueden utilizar algunas Actividades.
 */
public class NavDrawer
{
    private ArrayList<NavDrawerItem> items;
    private NavDrawerItem selectedItem;


    protected BaseActivity activity;
    protected DrawerLayout drawerLayout;

    protected ViewGroup navDrawerView;

    public NavDrawer(BaseActivity activity)
    {
        this.activity = activity;

        items = new ArrayList<>();

        drawerLayout = (DrawerLayout)activity.findViewById(R.id.drawer_layout);

        navDrawerView = (ViewGroup) activity.findViewById(R.id.nav_drawer);

        if (drawerLayout == null || navDrawerView == null)
        {
            throw new RuntimeException("Must have defined views witht he IDs: drawer_layout and nav_drawer");
        }

        Toolbar toolbar = activity.getToolbar();

        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setOpen(!isOpen());
            }
        });

        activity.getJaveShopApplication().getBus().register(this);

    }

    /**
     * Agrega un nuevo item al NavDrawer.
     * @param item item que se desea agregar.
     */
    public void addItem(NavDrawerItem item)
    {
        items.add(item);
        item.navDrawer = this;
    }

    /**
     * Si el NavDrawer esta abierto o cerrado.
     * @return true si esta abierto, false si esta cerrado.
     */
    public boolean isOpen()
    {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void setOpen(boolean isOpen)
    {
        if (isOpen)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        else
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * El ultimo item que escogio el usuario (la Actividad en la que esta actualmente)
     * @param item ultimo item seleccionado.
     */
    public void setSelectedItem(NavDrawerItem item)
    {
        if (selectedItem != null)
        {
            selectedItem.setSelected(false);
        }

        selectedItem = item;
        selectedItem.setSelected(true);
    }

    /**
     * Infla la interfaz del NavDrawer.
     */
    public void create()
    {
        LayoutInflater inflater = activity.getLayoutInflater();

        for (NavDrawerItem item : items)
        {
            item.inflate(inflater, navDrawerView);
        }
    }

    /**
     * Se elimina del bus de ventos para que no siga recibiendo eventos cuando no exista.
     */
    public void destroy()
    {
        activity.getJaveShopApplication().getBus().unregister(this);
    }

    /**
     * Representa un item del NavDrawer.
     */
    public static abstract class NavDrawerItem
    {
        protected NavDrawer navDrawer;

        public abstract void inflate(LayoutInflater inflater, ViewGroup navDrawerView);
        public abstract void setSelected(boolean isSelected);
    }


    /**
     * Tipo de NavDrawerItem que muestra texto y un icono.
     */
    public static class BasicNavDrawerItem extends NavDrawerItem implements View.OnClickListener
    {
        private String text;
        private int newNotifications;
        private int iconDrawable;
        private int containerId;

        private ImageView icon;
        private TextView textView;
        private TextView badgeTextview;
        private View view;
        private int defaultTextColor;

        public BasicNavDrawerItem(String text, int newNotifications, int iconDrawable, int containerId)
        {
            this.text = text;
            this.newNotifications = newNotifications;
            this.iconDrawable = iconDrawable;
            this.containerId = containerId;
        }

        /**
         * Infla la interfaz del item
         * @param inflater recurso del sistema que permite instanciar vistas.
         * @param navDrawerView contenedor del item.
         */
        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView)
        {
            ViewGroup container = (ViewGroup)navDrawerView.findViewById(containerId);

            if (container == null)
            {
                throw new RuntimeException("Container not found. Couldn't inflate view.");
            }


            view = inflater.inflate(R.layout.list_item_nav_drawer, container, false);

            container.addView(view);


            icon = (ImageView) view.findViewById(R.id.list_item_nav_drawer_icon);
            textView = (TextView) view.findViewById(R.id.list_item_nav_drawer_text);
            badgeTextview = (TextView) view.findViewById(R.id.list_item_nav_drawer_newNotifications);
            defaultTextColor = textView.getCurrentTextColor();


            icon.setImageResource(iconDrawable);
            textView.setText(text);


            if (newNotifications > 0)
            {
                badgeTextview.setText(Integer.toString(newNotifications));
            }
            else
            {
                badgeTextview.setVisibility(View.GONE);
            }

            view.setOnClickListener(this);
        }

        @Override
        public void setSelected(boolean isSelected)
        {
            if (isSelected)
            {
                view.setBackgroundResource(R.drawable.list_item_nav_drawer_selected_item_background);

                textView.setTextColor(navDrawer.activity.getResources().getColor(R.color.list_item_nav_drawer_selected_item_textColor));
            }
            else
            {
                view.setBackground(null);
                textView.setTextColor(defaultTextColor);
            }
        }

        public void setText(String text)
        {
            this.text = text;

            if (view != null)
            {
                textView.setText(text);
            }
        }

        public void setNewNotifications(int newNotifications)
        {
            this.newNotifications = newNotifications;

            if (view != null)
            {
                if (newNotifications > 0)
                {
                    badgeTextview.setVisibility(View.VISIBLE);
                }
                else
                {
                    badgeTextview.setVisibility(View.GONE);
                }
            }
        }

        public void setIcon(int iconDrawable)
        {
            this.iconDrawable = iconDrawable;

            if (view != null)
            {
                icon.setImageResource(iconDrawable);
            }
        }

        /**
         * Responde a eventos de clicks/touch.
         * @param v el View que fue tocado.
         */
        @Override
        public void onClick(View v)
        {
            navDrawer.setSelectedItem(this);


        }
    }

    /**
     * ActivityNavDrawerItems son BasicNavDrawerItems que instancian nuevas Actividades.
     */
    public static class ActivityNavDrawerItem extends BasicNavDrawerItem
    {
        private final Class targetActivity;
        private Intent intent;


        public ActivityNavDrawerItem(Class targetActivity, String text, int badge, int iconDrawable, int containerId)
        {
            super(text, badge, iconDrawable, containerId);
            this.targetActivity = targetActivity;
        }

        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView)
        {
            super.inflate(inflater, navDrawerView);

            if (this.navDrawer.activity.getClass() == targetActivity)
            {
                this.navDrawer.setSelectedItem(this);
            }
        }

        @Override
        public void onClick(View view)
        {
            navDrawer.setOpen(false);

            if (navDrawer.activity.getClass() == targetActivity)
            {
                return;
            }

            super.onClick(view);

            intent = new Intent(navDrawer.activity, targetActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            navDrawer.activity.startActivity(intent);
            navDrawer.activity.finish();
        }
    }
}
