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

}
