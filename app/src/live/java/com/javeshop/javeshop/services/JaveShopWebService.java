package com.javeshop.javeshop.services;

import com.google.gson.annotations.SerializedName;

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
     * @param request request del usuario
     * @param callback
     */
    @POST("/token.php")
    void login(@Body Account.LoginWithUsernameRequest request, Callback<Account.LoginWithUsernameResponse> callback);

    @POST("/api/v1/account.php")
    void register(@Body Account.RegisterRequest request, Callback<Account.RegisterResponse> callback);

    @POST("/login.php")
    void getAccount(@Body Account.LoginWithLocalTokenRequest request, Callback<Account.LoginWithLocalTokenResponse> callback);

    @PUT("/api/v1/account.php")
    void updateProfile(@Body Account.UpdateProfileRequest request, Callback<Account.UpdateProfileResponse> callback);

    @Multipart //Used to send chunks of files (file uploads)
    @POST("/api/v1/account/avatar.php")
    void updateAvatar(@Part("avatar")TypedFile avatar, Callback<Account.ChangeAvatarResponse> callback);

    @PUT("/api/v1/account/password.php")
    void updatePassword(@Body Account.ChangePasswordRequest request, Callback<Account.ChangePasswordResponse> callback);

    //----------------------------------------------------------------------------------------------------
    //Products
    //----------------------------------------------------------------------------------------------------
    @GET("/api/v1/products.php")
    void searchProduct(@Query("q") String query, Callback<Product.SearchProductResponse> callback);

}
