package com.javeshop.javeshop.services;

import android.widget.Toast;

import com.javeshop.javeshop.activities.LoginActivity;
import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.RetrofitCallback;
import com.javeshop.javeshop.infrastructure.RetrofitCallbackPost;
import com.javeshop.javeshop.infrastructure.User;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;

import retrofit.mime.TypedFile;

/**
 * Se encarga de manejar las solicitudes que tienen que ver con la cuenta del usuario.
 */
public class LiveAccountService extends BaseLiveService
{
    private final Auth auth;

    /**
     * Constructor
     * @param api interfaz por medio de la cual vamos a enviar los mensajes al servidor.
     * @param application instancia unica (Singleton) de nuestra aplicacion.
     */
    public LiveAccountService(JaveShopWebService api, JaveShopApplication application)
    {
        super(api, application);
        auth = application.getAuth();
    }

    /**
     * Escucha el evento para cuando un usuario se quiere registrar y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void register(Account.RegisterRequest request)
    {
        api.register(request, new RetrofitCallback<Account.RegisterResponse>(Account.RegisterResponse.class)
        {
            @Override
            protected void onResponse(Account.RegisterResponse registerResponse)
            {
                bus.post(registerResponse);
            }
        });
    }

    /**
     * Escucha el evento para cuando un usuario inicia sesion con correo y contraseña y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void loginWithUsername(Account.LoginWithUsernameRequest request)
    {
        api.login(request, new RetrofitCallback<Account.LoginWithUsernameResponse>(Account.LoginWithUsernameResponse.class)
        {
            @Override
            protected void onResponse(/*JaveShopWebService.LoginResponse loginResponse*/ Account.LoginWithUsernameResponse loginResponse)
            {
                //Toast.makeText(application, loginResponse.toString(), Toast.LENGTH_LONG).show();
                bus.post(loginResponse);
            }
        });
    }

    /**
     * Escucha el evento para cuando un usuario inicia sesion con un token y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void loginWithLocalToken(final Account.LoginWithLocalTokenRequest request)
    {
        //Toast.makeText(application, "Quiere login con local token: " + request.authToken, Toast.LENGTH_LONG).show();
        api.getAccount(request, new RetrofitCallbackPost<Account.LoginWithLocalTokenResponse>(Account.LoginWithLocalTokenResponse.class, bus)
        {
            @Override
            protected void onResponse(Account.LoginWithLocalTokenResponse loginWithLocalTokenResponse)
            {
                //Toast.makeText(application, /*loginWithLocalTokenResponse.toString()*/"Respondio el token", Toast.LENGTH_LONG).show();
                if (loginWithLocalTokenResponse.succeeded())
                {
                    loginUser(loginWithLocalTokenResponse);
                }
                else
                {
                    loginWithLocalTokenResponse.showErrorToast(application);
                }

                //super.onResponse(loginWithLocalTokenResponse);
                bus.post(loginWithLocalTokenResponse);
            }
        });
    }

    /**
     * Escucha el evento para cuando un usuario quiere editar sus datos personales y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void updateProfile(final Account.UpdateProfileRequest request)
    {
        api.updateProfile(request, new RetrofitCallbackPost<Account.UpdateProfileResponse>(Account.UpdateProfileResponse.class, bus)
        {
            @Override
            protected void onResponse(Account.UpdateProfileResponse response)
            {
                User user = auth.getUser();
                user.setFirstName(response.firstName);
                user.setLastName(response.lastName);
                user.setPhoneNumber(response.phoneNumber);
                super.onResponse(response);
                bus.post(new Account.UserDetailsUpdatedEvent(user));
            }
        });
    }

    /**
     * Escucha el evento para cuando un usuario quiere cambiar su avatar y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void updateAvatar(final Account.ChangeAvatarRequest request)
    {
        api.updateAvatar(new TypedFile("image/jpeg", new File(request.newAvatarUri.getPath())), new RetrofitCallbackPost<Account.ChangeAvatarResponse>(Account.ChangeAvatarResponse.class, bus)
        {
            @Override
            protected void onResponse(Account.ChangeAvatarResponse response)
            {
                bus.post(response);
            }
        });
    }

    /**
     * Escucha el evento para cuando un usuario quiere cambiar su contraseña y lo envia a la interfaz del web service para que el servidor procese la solicitud.
     * @param request solicitud del usuario con los datos relevantes.
     */
    @Subscribe
    public void changePassword(Account.ChangePasswordRequest request)
    {
        api.updatePassword(request, new RetrofitCallbackPost<Account.ChangePasswordResponse>(Account.ChangePasswordResponse.class, bus)
        {
            @Override
            protected void onResponse(Account.ChangePasswordResponse response)
            {
                bus.post(response);
                //super.onResponse(response);
            }
        });
    }

    /**
     * Esta funcion se llamaa cuando el usuario inicia sesion con token o con correo y contraseña, y sus datos han sido validados
     * @param response datos del usuario.
     */
    private void loginUser(Account.UserResponse response)
    {
        if (response.authToken != null && !response.authToken.isEmpty())
        {
            auth.setAuthToken(response.authToken);
        }

        User user = auth.getUser();
        user.setId(response.id);
        user.setFirstName(response.firstName);
        user.setLastName(response.lastName);
        user.setEmail(response.email);
        user.setAvatarUrl(response.avatarUrl);
        user.setPhoneNumber(response.phoneNumber);
        user.setBalance(response.balance);
        user.setReputation(response.reputation);
        user.setLoggedIn(true);


        bus.post(new Account.UserDetailsUpdatedEvent(user));
    }
}
