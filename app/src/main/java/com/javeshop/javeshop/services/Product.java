package com.javeshop.javeshop.services;

import com.javeshop.javeshop.services.entities.ProductDetails;

import java.util.List;

/**
 * Created by Jeffrey Torres on 14/10/2015.
 */
public class Product
{
    private Product()
    {}

    public static class PostProductRequest
    {
        public ProductDetails productDetails;

        public PostProductRequest(ProductDetails productDetails)
        {
            this.productDetails = productDetails;
        }
    }

    public static class PostProductResponse extends ServiceResponse
    {
    }

    public static class SearchProductRequest
    {
        public String query;

        public SearchProductRequest(String query)
        {
            this.query = query;
        }
    }

    public static class SearchProductResponse extends ServiceResponse
    {
        public String query;
        public List<ProductDetails> products;
    }

    public static class BuyProductRequest
    {
        public int buyerId;
        public float productId;

        public BuyProductRequest(int buyerId, float productId)
        {
            this.buyerId = buyerId;
            this.productId = productId;
        }
    }

    public static class BuyProductResponse extends ServiceResponse
    {
    }

    public static class QuantityChanged
    {
        public int value;

        public QuantityChanged(int value)
        {
            this.value = value;
        }
    }

}
