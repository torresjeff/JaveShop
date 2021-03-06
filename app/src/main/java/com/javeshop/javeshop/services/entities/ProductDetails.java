package com.javeshop.javeshop.services.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) que permite intercambiar informacion con el servidor.
 * En este DTO se lleva la informacion de un producto.
 */
public class ProductDetails implements Parcelable
{
    private int id;
    private int ownerId;
    private String ownerFirstName;
    private String ownerLastName;
    private String name;
    private String description;
    private String mainImageUrl;
    private ArrayList<String> productImagesUrls;
    private float price;
    private int quantity;
    private int state;
    private int category;



    public ProductDetails()
    {
        productImagesUrls = new ArrayList<>();
    }

    public ProductDetails(int ownerId, String name, String description, ArrayList<String> productImagesUrls, float price, int quantity, int state, int category)
    {
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.productImagesUrls = productImagesUrls;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
        this.category = category;
    }

    public ProductDetails(int id, int ownerId, String name, String description, String mainImageUrl, ArrayList<String> productImagesUrls, float price, int quantity, int state, int category)
    {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.productImagesUrls = productImagesUrls;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
        this.category = category;
    }

    protected ProductDetails(Parcel in)
    {
        id = in.readInt();
        ownerId = in.readInt();
        ownerFirstName = in.readString();
        ownerLastName = in.readString();
        name = in.readString();
        description = in.readString();
        mainImageUrl = in.readString();
        productImagesUrls = in.createStringArrayList();
        price = in.readFloat();
        quantity = in.readInt();
        state = in.readInt();
        category = in.readInt();
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>()
    {
        @Override
        public ProductDetails createFromParcel(Parcel in)
        {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size)
        {
            return new ProductDetails[size];
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

    public String getMainImageUrl()
    {
        return mainImageUrl;
    }

    public ArrayList<String> getProductImagesUrls()
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

    public int getCategory()
    {
        return category;
    }

    public String getOwnerFirstName()
    {
        return ownerFirstName;
    }

    public String getOwnerLastName()
    {
        return ownerLastName;
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
        parcel.writeString(ownerFirstName);
        parcel.writeString(ownerLastName);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(mainImageUrl);
        parcel.writeStringList(productImagesUrls);
        parcel.writeFloat(price);
        parcel.writeInt(quantity);
        parcel.writeInt(state);
        parcel.writeInt(category);
    }
}
