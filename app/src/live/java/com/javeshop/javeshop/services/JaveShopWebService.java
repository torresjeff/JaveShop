package com.javeshop.javeshop.services;

import com.google.gson.annotations.SerializedName;
import com.javeshop.javeshop.infrastructure.RetrofitCallbackPost;
import com.javeshop.javeshop.services.entities.ProductComment;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Representa la interfaz por la cual se llamarán los servicios web de JaveShop.
 */
public interface JaveShopWebService
{
    //----------------------------------------------------------------------------------------------------
    //Accounts
    //----------------------------------------------------------------------------------------------------
    /**
     * Genera un token de acceso a la aplicación.
     * @param request representa la solicitud del usuario, con los datos relevantes a su solicitud.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @POST("/token.php")
    void login(@Body Account.LoginWithUsernameRequest request, Callback<Account.LoginWithUsernameResponse> callback);

    /**
     * Envia un request al servidor para registrarse en el sistema. El servidor le envia un correo al usuario.
     * @param request representa la solicitud del usuario, con los datos relevantes a su solicitud.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @POST("/api/v1/account.php")
    void register(@Body Account.RegisterRequest request, Callback<Account.RegisterResponse> callback);

    /**
     * Recibe los datos personales de un usuario cuando inicia sesion.
     * @param request representa la solicitud del usuario, con los datos relevantes a su solicitud.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @POST("/login.php")
    void getAccount(@Body Account.LoginWithLocalTokenRequest request, Callback<Account.LoginWithLocalTokenResponse> callback);

    /**
     * Envia una solicitud al servidor para actualizar los datos personales del usuario.
     * @param request representa la solicitud del usuario, con los datos relevantes a su solicitud.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @PUT("/api/v1/account.php")
    void updateProfile(@Body Account.UpdateProfileRequest request, Callback<Account.UpdateProfileResponse> callback);

    /**
     * Solicitud para actualizar el avatar de un usuario.
     * @param avatar la imagen que representa el nevo avatar
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @Multipart //Used to send chunks of files (file uploads)
    @POST("/api/v1/account/avatar.php")
    void updateAvatar(@Part("avatar")TypedFile avatar, Callback<Account.ChangeAvatarResponse> callback);

    /**
     * Solicitud para actualizar la contraseña de un usuario.
     * @param request representa la solicitud del usuario, con los datos relevantes a su solicitud.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @PUT("/api/v1/account/password.php")
    void updatePassword(@Body Account.ChangePasswordRequest request, Callback<Account.ChangePasswordResponse> callback);

    //----------------------------------------------------------------------------------------------------
    //Products
    //----------------------------------------------------------------------------------------------------

    /**
     * Solicitud para buscar un producto con un nombre especificado.
     * @param query contiene el nombre del producto.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @GET("/api/v1/products.php")
    void searchProduct(@Query("q") String query, Callback<Product.SearchProductResponse> callback);

    /**
     * Solicitud para ver los comentarios de un producto.
     * @param id el id del producto del cual queremos ver los comentarios.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @GET("/api/v1/products/comments.php")
    void getComments(@Query("id") int id, Callback<Product.GetProductCommentsResponse> callback);

    /**
     * Solicitud para publicar un comentario en un producto.
     * @param comment comentario que se quiere publicar.
     * @param callback objeto en el cual recibiremos la respuesta del servidor.
     */
    @POST("/api/v1/products/comments.php")
    void postComment(@Body ProductComment comment, Callback<Product.SendCommentResponse> callback);

    /**
     * Solicitud para responder un comentario. El comentario solo lo puedo responder el dueño del producto.
     * @param request
     * @param callback
     */
    @PUT("/api/v1/products/comments.php")
    void replyComment(@Body Product.ReplyCommentRequest request, Callback<Product.ReplyCommentResponse> callback);

    /**
     * Solicitud para ver los favoritos del usuario que esta actualmente conectado.
     * @param callback
     */
    @GET("/api/v1/products/favorites.php")
    void getFavorites(Callback<Product.GetFavoritesResponse> callback);

    /**
     * Solicitud para agregar o eliminar un producto de los favoritos.
     * @param request
     * @param callback
     */
    @PUT("/api/v1/products/favorites.php")
    void markAsFavorite(@Body Product.MarkAsFavoriteRequest request, Callback<Product.MarkAsFavoriteResponse> callback);

    /**
     * Solicitud para comprar un producto.
     * @param request
     * @param callback
     */
    @POST("/api/v1/products/buy.php")
    void buyProduct(@Body Product.BuyProductRequest request, Callback<Product.BuyProductResponse> callback);

    /**
     * Solicitud para ver los productos que ha comprado un usuario.
     * @param callback
     */
    @GET("/api/v1/products/buy.php")
    void getBoughtProducts(RetrofitCallbackPost<Product.GetBoughtProductsResponse> callback);

    /**
     * Solicitud para ver los productos publicados de un usuario.
     * @param callback
     */
    @GET("/api/v1/products/sold.php")
    void getSoldProducts(RetrofitCallbackPost<Product.GetPostedProductsResponse> callback);

    /**
     * Solicitud para publicar un producto.
     * @param request
     * @param callback
     */
    @POST("/api/v1/products/publish.php")
    void publishProduct(@Body MultipartTypedOutput request, RetrofitCallbackPost<Product.PostProductResponse> callback);


    //----------------------------------------------------------------------------------------------------
    //Users
    //----------------------------------------------------------------------------------------------------
    /**
     * Solicitud para ver los datos publicos del vendedor de un producto.
     * @param userId
     * @param callback
     */
    @GET("/api/v1/users.php")
    void searchUser(@Query("id") int userId, RetrofitCallbackPost<Users.GetUserDetailsResponse> callback);

    /**
     * Solicitud para calificar a un usuario.
     * @param request
     * @param callback
     */
    @POST("/api/v1/users.php")
    void rateUser(@Body Users.RateUserRequest request, RetrofitCallbackPost<Users.RateUserResponse> callback);

}
