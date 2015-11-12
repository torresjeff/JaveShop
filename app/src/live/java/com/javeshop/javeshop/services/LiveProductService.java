package com.javeshop.javeshop.services;

import android.util.Log;

import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.infrastructure.RetrofitCallbackPost;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 1/11/2015.
 */
public class LiveProductService extends BaseLiveService
{
    private final Auth auth;

    public LiveProductService(JaveShopWebService api, JaveShopApplication application)
    {
        super(api, application);
        auth = application.getAuth();
    }

    @Subscribe
    public void searchProduct(Product.SearchProductRequest request)
    {
        api.searchProduct(request.query, new RetrofitCallbackPost<Product.SearchProductResponse>(Product.SearchProductResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.SearchProductResponse response)
            {
                bus.post(response);
                //Log.e("LiveProductService", "Response: firstName=" + response.products.get(0).getOwnerFirstName() + ",lastName=" + response.products.get(0).getOwnerLastName());
                //super.onResponse(response);
            }
        });
    }
}
