package com.javeshop.javeshop.services;

import com.javeshop.javeshop.services.entities.ProductComment;
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
        public int quantity;

        public BuyProductRequest(int buyerId, float productId, int quantity)
        {
            this.buyerId = buyerId;
            this.productId = productId;
            this.quantity = quantity;
        }
    }

    public static class BuyProductResponse extends ServiceResponse
    {
    }

    public static class MarkAsFavoriteRequest
    {
        public int userId;
        public int productId;

        public MarkAsFavoriteRequest(int userId, int productId)
        {
            this.userId = userId;
            this.productId = productId;
        }
    }

    public static class MarkAsFavoriteResponse extends ServiceResponse
    {
        //TODO: este response debe responder con un id diciendo si fue agregado o eliminado
    }

    public static class QuantityChanged
    {
        public int value;

        public QuantityChanged(int value)
        {
            this.value = value;
        }
    }

    public static class GetProductCommentsRequest
    {
        public int productId;
        public GetProductCommentsRequest(int productId)
        {
            this.productId = productId;
        }
    }

    public static class GetProductCommentsResponse extends ServiceResponse
    {
        public List<ProductComment> comments;
    }

    public static class SendCommentRequest
    {
        public ProductComment comment;
        public SendCommentRequest(ProductComment comment)
        {
            this.comment = comment;
        }
    }

    public static class SendCommentResponse extends ServiceResponse
    {
        public ProductComment comment;
    }

    public static class GetFavoritesRequest
    {
        public int userId;

        public GetFavoritesRequest(int userId)
        {
            this.userId = userId;
        }
    }

    public static class GetFavoritesResponse extends ServiceResponse
    {
        public List<ProductDetails> products;
    }

    public static class GetPostedProductsRequest
    {
        public int userId;

        public GetPostedProductsRequest(int userId)
        {
            this.userId = userId;
        }
    }

    public static class GetPostedProductsResponse extends ServiceResponse
    {
        public List<ProductDetails> products;
    }

    public static class UpdateProductDetailsRequest
    {
        public ProductDetails productDetails;

        public UpdateProductDetailsRequest(ProductDetails productDetails)
        {
            this.productDetails = productDetails;
        }
    }

    public static class UpdateProductDetailsResponse extends ServiceResponse
    {
    }
}
