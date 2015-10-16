package com.javeshop.javeshop.services.entities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey Torres on 14/10/2015.
 */
public class ProductDetailsSell implements Parcelable
{
    private int id;
    private int ownerId;
    private String name;
    private String description;
    private List<Uri> productImagesUrls;
    private float price;
    private int quantity;
    private int state;

    //TODO: verificar que el constructor vacio no altere los responses del servidor
    public ProductDetailsSell()
    {
        productImagesUrls = new ArrayList<>();
    }

    public ProductDetailsSell(int id, int ownerId, String name, String description, List<Uri> productImagesUrls, float price, int quantity, int state)
    {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.productImagesUrls = productImagesUrls;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
    }

    protected ProductDetailsSell(Parcel in)
    {
        id = in.readInt();
        ownerId = in.readInt();
        name = in.readString();
        description = in.readString();
        productImagesUrls = in.createTypedArrayList(Uri.CREATOR);
        price = in.readFloat();
        quantity = in.readInt();
        state = in.readInt();
    }

    public static final Creator<ProductDetailsSell> CREATOR = new Creator<ProductDetailsSell>()
    {
        @Override
        public ProductDetailsSell createFromParcel(Parcel in)
        {
            return new ProductDetailsSell(in);
        }

        @Override
        public ProductDetailsSell[] newArray(int size)
        {
            return new ProductDetailsSell[size];
        }
    };

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }


    public List<Uri> getProductImagesUris()
    {
        return productImagesUrls;
    }

    public float getPrice()
    {
        return price;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public int getState()
    {
        return state;
    }

    public int getOwnerId()
    {
        return ownerId;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {

        parcel.writeInt(id);
        parcel.writeInt(ownerId);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeTypedList(productImagesUrls);
        parcel.writeFloat(price);
        parcel.writeInt(quantity);
        parcel.writeInt(state);
    }
}
