package com.javeshop.javeshop.services;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.services.entities.ProductComment;
import com.javeshop.javeshop.services.entities.ProductDetails;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Emula las respuestas del servidor que tienen que ver con la informacion de productos.
 */
public class InMemoryProductService extends BaseInMemoryService
{
    ArrayList<ProductDetails> products;

    public InMemoryProductService(JaveShopApplication application)
    {
        super(application);

        products = new ArrayList<>();
    }

    /**
     * Busca el producto que un usuario ingreso en la barra de busqueda.
     * Se envia un evento que emula la respuesta del servidor. La respuesta contiene el resultado de la consulta (una lista de ProductDetails).
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void searchProduct(Product.SearchProductRequest request)
    {
        Product.SearchProductResponse response = new Product.SearchProductResponse();
        response.query = request.query;
        response.products = new ArrayList<>();

        for (int i = 0; i < request.query.length(); ++i)
        {
            ArrayList<String> images = new ArrayList<>();
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+1) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+2) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+3) + "?d=identicon&s=600");
            response.products.add(
                    new ProductDetails(
                            i,
                            i,
                            request.query + " " + (i+1),
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Phasellus fermentum odio mauris, ac lacinia quam elementum quis. " +
                                    "Vestibulum feugiat arcu.",
                            "http://www.gravatar.com/avatar/" + Integer.toString(i) + "?d=identicon&s=600",
                            images, 10000*(i+1), i+1, i%2, (i%23)+1));
        }

        postDelayed(response, 1000, 2000);
    }

    /**
     * Compra el producto que un usuario escoge.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void buyProduct(Product.BuyProductRequest request)
    {
        postDelayed(new Product.BuyProductResponse(), 1000, 2000);
    }

    /**
     * Publica el producto con toda la informacion e imagenes que el usuario ha especificado.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void postProduct(Product.PostProductRequest request)
    {
        postDelayed(new Product.PostProductResponse(), 1000, 2000);
    }

    /**
     * Elimina un producto que un usuario habia previamente publicado.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void deleteProduct(Product.DeleteProductRequest request)
    {
        postDelayed(new Product.DeleteProductResponse(), 1000, 1500);
    }

    /**
     * Envia un request para marcar un producto como favorito.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void markAsFavorite(Product.MarkAsFavoriteRequest request)
    {
        postDelayed(new Product.MarkAsFavoriteResponse(), 200, 300);
    }

    /**
     * Envia un request para cargar todos los comentarios de un producto en especifico.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void getComments(Product.GetProductCommentsRequest request)
    {
        Product.GetProductCommentsResponse response = new Product.GetProductCommentsResponse();
        response.comments = new ArrayList<>();

        for (int i = 0; i < 5; ++i)
        {
            response.comments.add(new ProductComment(request.productId, i, 0, "Pregunta " + i, 1, i %2 == 0 ? "Respuesta " + i : null));
        }

        postDelayed(response);
    }

    /**
     * Envia un request para publicar un nuevo comentario en un producto.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void postComment(Product.SendCommentRequest request)
    {
        Product.SendCommentResponse response = new Product.SendCommentResponse();
        response.comment = request.comment;
        postDelayed(response);
    }

    /**
     * Envia un request para cargar todos los productos que un usuario ha marcado como favoritos.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void getFavorites(Product.GetFavoritesRequest request)
    {
        Product.GetFavoritesResponse response = new Product.GetFavoritesResponse();
        response.products = new ArrayList<>();

        for (int i = 0; i < 10; ++i)
        {
            ArrayList<String> images = new ArrayList<>();
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+1) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+2) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+3) + "?d=identicon&s=600");
            response.products.add(
                    new ProductDetails(
                            i,
                            i,
                            "Favorito " + (i+1),
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Phasellus fermentum odio mauris, ac lacinia quam elementum quis. " +
                                    "Vestibulum feugiat arcu.",
                            "http://www.gravatar.com/avatar/" + Integer.toString(i) + "?d=identicon&s=600",
                            images, 10000*(i+1), i, i%2, (i%23)+1));
        }

        postDelayed(response, 1000, 2000);
    }

    /**
     * Envia un request para cargar todos los productos que un usuario ha publicado.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void getPostedProducts(Product.GetPostedProductsRequest request)
    {
        Product.GetPostedProductsResponse response = new Product.GetPostedProductsResponse();
        response.products = new ArrayList<>();

        for (int i = 0; i < 8; ++i)
        {
            ArrayList<String> images = new ArrayList<>();
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+1) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+2) + "?d=identicon&s=600");
            images.add("http://www.gravatar.com/avatar/" + Integer.toString(i+3) + "?d=identicon&s=600");
            response.products.add(
                    new ProductDetails(
                            i,
                            i,
                            "Producto " + (i+1),
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Phasellus fermentum odio mauris, ac lacinia quam elementum quis. " +
                                    "Vestibulum feugiat arcu.",
                            "http://www.gravatar.com/avatar/" + Integer.toString(i) + "?d=identicon&s=600",
                            images, 10000*(i+1), i, i%2, (i%23)+1));
        }

        postDelayed(response, 1000, 2000);
    }

    /**
     * Envia un request para actualizar los datos de un producto que un usuario ha publicado previamente.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void updateProductDetails(Product.UpdateProductDetailsRequest request)
    {
        postDelayed(new Product.UpdateProductDetailsResponse(), 1000, 2000);
    }

}
