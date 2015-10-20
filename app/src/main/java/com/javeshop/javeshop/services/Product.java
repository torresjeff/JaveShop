package com.javeshop.javeshop.services;

import com.javeshop.javeshop.services.entities.ProductComment;
import com.javeshop.javeshop.services.entities.ProductDetails;

import java.util.List;

/**
 * Contiene clases estaticas que representan los requests/responses que tienen que ver con los productos y la informacion que debe contener cada uno para comunicarse con el servidor.
 */
public class Product
{
    private Product()
    {}

    /**
     * Request para publicar un producto. Contiene todos los detalles que debe llenar el usuario para poder publicar un producto.
     */
    public static class PostProductRequest
    {
        public ProductDetails productDetails;

        public PostProductRequest(ProductDetails productDetails)
        {
            this.productDetails = productDetails;
        }
    }

    /**
     * Respuesta del servidor a PostProductRequest.
     */
    public static class PostProductResponse extends ServiceResponse
    {
    }

    /**
     * Request para buscar un producto (por nombre).
     */
    public static class SearchProductRequest
    {
        public String query;

        public SearchProductRequest(String query)
        {
            this.query = query;
        }
    }

    /**
     * Respuesta del servidor a SearchProductRequest. Responde con la lista de productos encontrados.
     */
    public static class SearchProductResponse extends ServiceResponse
    {
        public String query;
        public List<ProductDetails> products;
    }

    /**
     * Request para comprar un producto. Contiene la informacion basica para registrar la transaccion.
     */
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

    /**
     * Respuesta del servidor a BuyProductRequest.
     */
    public static class BuyProductResponse extends ServiceResponse
    {
    }


    /**
     * Request para marcar un producto como favorito.
     */
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

    /**
     * Respuesta del servidor a MarkAsFavoriteRequest.
     */
    public static class MarkAsFavoriteResponse extends ServiceResponse
    {
        //TODO: este response debe responder con un id diciendo si fue agregado o eliminado
    }


    /**
     * Evento que se dispara cuando un usuario cambia la cantidad de unidades disponibles
     */
    public static class QuantityChanged
    {
        public int value;

        public QuantityChanged(int value)
        {
            this.value = value;
        }
    }

    /**
     * Request para ver los comentarios de un producto especifico.
     */
    public static class GetProductCommentsRequest
    {
        public int productId;
        public GetProductCommentsRequest(int productId)
        {
            this.productId = productId;
        }
    }

    /**
     * Respuesta del servidor a GetProductCommentsResponse. Retorna la lista de comentarios del producto.
     */
    public static class GetProductCommentsResponse extends ServiceResponse
    {
        public List<ProductComment> comments;
    }

    /**
     * Request para publicar un comentario en un producto.
     */
    public static class SendCommentRequest
    {
        public ProductComment comment;
        public SendCommentRequest(ProductComment comment)
        {
            this.comment = comment;
        }
    }

    /**
     * Respuesta del servidor a SendCommentRequest. Responde con el comentario hecho para actualizar la interfaz acordemente.
     */
    public static class SendCommentResponse extends ServiceResponse
    {
        public ProductComment comment;
    }

    /**
     * Request para ver todos los favoritos de un usuario.
     */
    public static class GetFavoritesRequest
    {
        public int userId;

        public GetFavoritesRequest(int userId)
        {
            this.userId = userId;
        }
    }

    /**
     * Respuesta del servidor a GetFavoritesRequest. Responde con la lista de productos que son marcados como favoritos por el usuario.
     */
    public static class GetFavoritesResponse extends ServiceResponse
    {
        public List<ProductDetails> products;
    }

    /**
     * Request para ver todos los productos que un usuario tiene actualmente publicados.
     */
    public static class GetPostedProductsRequest
    {
        public int userId;

        public GetPostedProductsRequest(int userId)
        {
            this.userId = userId;
        }
    }

    /**
     * Respuesta del servidor a GetPostedProductsRequest. Responde con la lista de productos que actualmente el usuario tiene publicados.
     */
    public static class GetPostedProductsResponse extends ServiceResponse
    {
        public List<ProductDetails> products;
    }

    /**
     * Request para actualizar los datos de un producto ya publicado.
     */
    public static class UpdateProductDetailsRequest
    {
        public ProductDetails productDetails;

        public UpdateProductDetailsRequest(ProductDetails productDetails)
        {
            this.productDetails = productDetails;
        }
    }

    /**
     * Respuesta del servidor a UpdateProductDetailsRequest.
     */
    public static class UpdateProductDetailsResponse extends ServiceResponse
    {
    }
}
