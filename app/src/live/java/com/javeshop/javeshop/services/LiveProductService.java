package com.javeshop.javeshop.services;

import android.util.Log;

import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.infrastructure.RetrofitCallbackPost;
import com.javeshop.javeshop.services.entities.ProductComment;
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
                //TODO: el toast de cuando no encuentra ningun producto se mustra dos veces
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void getComments(Product.GetProductCommentsRequest request)
    {
        api.getComments(request.productId, new RetrofitCallbackPost<Product.GetProductCommentsResponse>(Product.GetProductCommentsResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.GetProductCommentsResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void getFavorites(Product.GetFavoritesRequest request)
    {
        api.getFavorites(new RetrofitCallbackPost<Product.GetFavoritesResponse>(Product.GetFavoritesResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.GetFavoritesResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void markAsFavorite(Product.MarkAsFavoriteRequest request)
    {
        api.markAsFavorite(request, new RetrofitCallbackPost<Product.MarkAsFavoriteResponse>(Product.MarkAsFavoriteResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.MarkAsFavoriteResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void postComment(Product.SendCommentRequest request)
    {
        api.postComment(request.comment, new RetrofitCallbackPost<Product.SendCommentResponse>(Product.SendCommentResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.SendCommentResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void replyComment(Product.ReplyCommentRequest request)
    {
        api.replyComment(request, new RetrofitCallbackPost<Product.ReplyCommentResponse>(Product.ReplyCommentResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.ReplyCommentResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void buyProduct(Product.BuyProductRequest request)
    {
        api.buyProduct(request, new RetrofitCallbackPost<Product.BuyProductResponse>(Product.BuyProductResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.BuyProductResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void getBoughtProducts(Product.GetBoughtProductsRequest request)
    {
        api.getBoughtProducts(new RetrofitCallbackPost<Product.GetBoughtProductsResponse>(Product.GetBoughtProductsResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.GetBoughtProductsResponse response)
            {
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void getSoldProducts(Product.GetPostedProductsRequest request)
    {
        api.getSoldProducts(new RetrofitCallbackPost<Product.GetPostedProductsResponse>(Product.GetPostedProductsResponse.class, bus)
        {
            @Override
            protected void onResponse(Product.GetPostedProductsResponse response)
            {
                bus.post(response);
            }
        });
    }
}
