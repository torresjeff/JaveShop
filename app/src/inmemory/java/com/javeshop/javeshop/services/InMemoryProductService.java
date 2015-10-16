package com.javeshop.javeshop.services;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Jeffrey Torres on 14/10/2015.
 */
public class InMemoryProductService extends BaseInMemoryService
{
    public InMemoryProductService(JaveShopApplication application)
    {
        super(application);
    }

    @Subscribe
    public void searchProduct(Product.SearchProductRequest request)
    {
        Product.SearchProductResponse response = new Product.SearchProductResponse();
        response.products = new ArrayList<>();

        for (int i = 0; i < request.query.length(); ++i)
        {
            response.products.add(
                    new ProductDetails(
                            i,
                            i,
                            request.query + " " + (i+1),
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Phasellus fermentum odio mauris, ac lacinia quam elementum quis. " +
                                    "Vestibulum feugiat arcu.",
                            "http://www.gravatar.com/avatar/" + Integer.toString(i) + "?d=identicon&s=64",
                            null, 10000*(i+1), i, i%2));
        }

        postDelayed(response, 1000, 2000);
    }

    @Subscribe
    public void buyProduct(Product.BuyProductRequest request)
    {
        postDelayed(new Product.BuyProductResponse(), 1000, 2000);
    }
}