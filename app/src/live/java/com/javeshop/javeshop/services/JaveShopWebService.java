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

    /**
     * Genera un token de acceso a la aplicación.
     * @param email
     * @param password
     * @param callback
     */
    @POST("/token.php") //POST = when you send/submit a form, CREATE
    @FormUrlEncoded
    //Specific type of encoding that most browsers understand
    void login(
            @Field("email") String email,
            @Field("password") String password,
            Callback<LoginResponse> callback);

    @POST("/api/v1/account.php")
    void register(@Body Account.RegisterRequest request, Callback<Account.RegisterResponse> callback);

    @GET("/api/v1/account")
        //Retrieves account information
    void getAccount(Callback<Account.LoginWithLocalTokenResponse> callback);

    @PUT("/api/v1/account")
    void updateProfile(@Body Account.UpdateProfileRequest request, Callback<Account.UpdateProfileResponse> callback);

    @Multipart //Used to send chunks of files (file uploads)
    @PUT("/api/v1/account/avatar")
    void updateAvatar(@Part("avatar")TypedFile avatar, Callback<Account.ChangeAvatarResponse> callback);

    @PUT("/api/v1/account/password")
    void updatePassword(@Body Account.ChangePasswordRequest request, Callback<Account.ChangePasswordResponse> callback);

    //----------------------------------------------------------------------------------------------------
    //DTOs

    class LoginResponse extends ServiceResponse
    {
        @SerializedName("token")
        public String token;

        //TODO: enable errors
        /*@SerializedName("error")
        public String error;

        @SerializedName("error_description")
        public String errorDescription;*/
    }
}
