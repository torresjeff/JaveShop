package com.javeshop.javeshop.services;

import android.widget.Toast;

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
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class LiveAccountService extends BaseLiveService
{
    private final Auth auth;

    public LiveAccountService(JaveShopWebService api, JaveShopApplication application)
    {
        super(api, application);
        auth = application.getAuth();
    }

    @Subscribe
    public void register(Account.RegisterRequest request)
    {
        api.register(request, new RetrofitCallback<Account.RegisterResponse>(Account.RegisterResponse.class)
        {
            @Override
            protected void onResponse(Account.RegisterResponse registerResponse)
            {
                bus.post(registerResponse);

                //TODO: quitar el progressBar
                Toast.makeText(application, registerResponse.toString(), Toast.LENGTH_SHORT).show();
                if (registerResponse.succeeded())
                {
                    //TODO.
                }
            }
        });
    }

    @Subscribe
    public void loginWithUsername(final Account.LoginWithUsernameRequest request)
    {
        api.login(request.username, request.password, new RetrofitCallback<JaveShopWebService.LoginResponse>(JaveShopWebService.LoginResponse.class)
        {
            @Override
            protected void onResponse(JaveShopWebService.LoginResponse loginResponse)
            {
                if (!loginResponse.succeeded())
                {
                    Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();
                    //TODO: volver a enable
                    //response.setPropertyError("userName", loginResponse.errorDescription);
                    bus.post(response);
                    return;
                }

                auth.setAuthToken(loginResponse.token);
                api.getAccount(new RetrofitCallback<Account.LoginWithLocalTokenResponse>(Account.LoginWithLocalTokenResponse.class)
                {
                    @Override
                    protected void onResponse(Account.LoginWithLocalTokenResponse loginWithLocalTokenResponse)
                    {
                        if (!loginWithLocalTokenResponse.succeeded())
                        {
                            Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();
                            response.setOperationError(loginWithLocalTokenResponse.getOperationError());
                            bus.post(response);
                            return;
                        }

                        loginUser(loginWithLocalTokenResponse);
                        bus.post(new Account.LoginWithUsernameResponse());
                    }
                });
            }
        });
    }

    @Subscribe
    public void loginWithLocalToken(final Account.LoginWithLocalTokenRequest request)
    {
        api.getAccount(new RetrofitCallbackPost<Account.LoginWithLocalTokenResponse>(Account.LoginWithLocalTokenResponse.class, bus)
        {
            @Override
            protected void onResponse(Account.LoginWithLocalTokenResponse loginWithLocalTokenResponse)
            {
                loginUser(loginWithLocalTokenResponse);
                super.onResponse(loginWithLocalTokenResponse);
            }
        });
    }

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

    @Subscribe
    public void updateAvatar(final Account.ChangeAvatarRequest request)
    {
        api.updateAvatar(new TypedFile("image/jpeg", new File(request.newAvatarUri.getPath())), new RetrofitCallbackPost<Account.ChangeAvatarResponse>(Account.ChangeAvatarResponse.class, bus)
        {
            @Override
            protected void onResponse(Account.ChangeAvatarResponse response)
            {
                User user = auth.getUser();
                user.setAvatarUrl(response.avatarUrl);
                super.onResponse(response);
                bus.post(new Account.UserDetailsUpdatedEvent(user));
            }
        });
    }

    @Subscribe
    public void changePassword(Account.ChangePasswordRequest request)
    {
        api.updatePassword(request, new RetrofitCallbackPost<Account.ChangePasswordResponse>(Account.ChangePasswordResponse.class, bus)
        {
            @Override
            protected void onResponse(Account.ChangePasswordResponse response)
            {
                if (response.succeeded())
                {
                    bus.post(response);
                }

                super.onResponse(response);
            }
        });
    }

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
