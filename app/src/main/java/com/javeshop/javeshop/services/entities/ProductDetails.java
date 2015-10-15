package com.javeshop.javeshop.services.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Jeffrey Torres on 14/10/2015.
 */
public class ProductDetails implements Parcelable
{
    private int id;
    private String name;
    private String description;
    private String mainImageUrl;
    private List<String> productImagesUrls;
    private float price;
    private int quantity;
    private int state;

    public ProductDetails(int id, String name, String description, String mainImageUrl, List<String> productImagesUrls, float price, int quantity, int state)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.productImagesUrls = productImagesUrls;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
    }

    protected ProductDetails(Parcel in)
    {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        mainImageUrl = in.readString();
        productImagesUrls = in.createStringArrayList();
        price = in.readFloat();
        quantity = in.readInt();
        state = in.readInt();
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

    public List<String> getProductImagesUrls()
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

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(mainImageUrl);
        parcel.writeStringList(productImagesUrls);
        parcel.writeFloat(price);
        parcel.writeInt(quantity);
        parcel.writeInt(state);
    }
}
