package com.javeshop.javeshop.services;

import android.util.Log;

import com.javeshop.javeshop.infrastructure.Auth;
import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.infrastructure.User;
import com.squareup.otto.Subscribe;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class InMemoryAccountService extends BaseInMemoryService
{
    public InMemoryAccountService(JaveShopApplication application)
    {
        super(application);
    }


    @Subscribe
    public void loginWithLocalToken(Account.LoginWithLocalTokenRequest request)
    {
        invokeDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Account.LoginWithLocalTokenResponse response = new Account.LoginWithLocalTokenResponse();
                loginUser(response);
                bus.post(response);
            }
        }, 1000, 2000);
    }

    @Subscribe
    public void loginWithUsername(Account.LoginWithUsernameRequest request)
    {
        invokeDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();
                loginUser(new Account.UserResponse());
                bus.post(response);
            }
        }, 1000, 2000);
    }

    private void loginUser(Account.UserResponse response)
    {
        Auth auth = application.getAuth();
        User user = auth.getUser();

        user.setFirstName("Jeffrey");
        user.setLastName("Torres");
        user.setPhoneNumber("1234567");
        user.setBalance(100000);
        user.setReputation(5);
        user.setEmail("torres.jeffrey@javeriana.edu.co");
        user.setAvatarUrl("http://www.gravatar.com/avatar/1?d=identicon");
        user.setId(123);
        user.setLoggedIn(true);

        bus.post(new Account.UserDetailsUpdatedEvent(user));

        auth.setAuthToken("token");

        response.firstName = user.getFirstName();
        response.lastName = user.getLastName();
        response.phoneNumber = user.getPhoneNumber();
        response.balance = user.getBalance();
        response.reputation = user.getReputation();
        response.email = user.getEmail();
        response.avatarUrl = user.getAvatarUrl();
        response.id = user.getId();
        response.authToken = auth.getAuthToken();
    }

    @Subscribe
    public void updateAvatar(final Account.ChangeAvatarRequest request)
    {
        invokeDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                User user = application.getAuth().getUser();
                user.setAvatarUrl(request.newAvatarUri.toString());

                Account.ChangeAvatarResponse response = new Account.ChangeAvatarResponse();
                response.avatarUrl = request.newAvatarUri.toString();
                //Log.e("InMemoryAccountService", "response avatarUrl: " + response.avatarUrl + ", requestAvatarUrl: " + request.newAvatarUri.toString());

                bus.post(response);
                bus.post(new Account.UserDetailsUpdatedEvent(user));
            }
        }, 3000, 4000);
    }

    @Subscribe
    public void updateProfile(final Account.UpdateProfileRequest request)
    {
        final Account.UpdateProfileResponse response = new Account.UpdateProfileResponse();

        //Si quieren simular errores, llenamos el response con errores en su HashMap

        invokeDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                User user = application.getAuth().getUser();
                user.setFirstName(request.firstName);
                user.setLastName(request.lastName);
                user.setPhoneNumber(request.phoneNumber);

                bus.post(response);
                bus.post(new Account.UserDetailsUpdatedEvent(user));
            }
        }, 2000, 3000);
    }

    @Subscribe
    public void changePassword(Account.ChangePasswordRequest request)
    {
        Account.ChangePasswordResponse response = new Account.ChangePasswordResponse();

        if (!request.newPassword.equals(request.confirmNewPassword))
        {
            response.setPropertyError("confirmNewPassword", "Las contraseñas no coinciden");
        }

        if (request.newPassword.length() < 3)
        {
            response.setPropertyError("newPassword", "La contraseña debe tener más de 3 caracteres");
        }

        postDelayed(response);
    }
}
