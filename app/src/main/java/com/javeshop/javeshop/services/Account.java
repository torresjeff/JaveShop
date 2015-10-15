package com.javeshop.javeshop.services;

import android.net.Uri;

import com.javeshop.javeshop.infrastructure.User;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class Account
{
    private Account()
    {
    }

    /**
     * Contiene toda la información necesaria cuando el usuario inicia sesión. El servidor response con esta información.
     */
    public static class UserResponse extends ServiceResponse
    {
        public int id;
        public String avatarUrl;
        public String firstName;
        public String lastName;
        public String phoneNumber;
        public float balance;
        public float reputation;
        public String email;
        public String authToken;
    }

    public static class LoginWithUsernameRequest
    {
        public String username;
        public String password;

        public LoginWithUsernameRequest(String username, String password)
        {
            this.username = username;
            this.password = password;
        }
    }

    public static class LoginWithUsernameResponse extends UserResponse
    {
    }

    //El token previene que el usuario tenga que loggearse cada vez que se sale de la application.
    public static class LoginWithLocalTokenRequest
    {
        public String authToken;

        public LoginWithLocalTokenRequest(String authToken)
        {
            this.authToken = authToken;
        }
    }

    public static class LoginWithLocalTokenResponse extends UserResponse
    {
    }

    public static class RegisterRequest
    {
        public String firstName;
        public String lastName;
        public String email;
        public String password;
        public RegisterRequest(String firstName, String lastName, String email, String password)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }

    }

    public static class RegisterResponse extends ServiceResponse
    {
    }

    public static class ChangeAvatarRequest
    {

        public Uri newAvatarUri;
        public ChangeAvatarRequest(Uri newAvatarUri)
        {
            this.newAvatarUri = newAvatarUri;
        }

    }

    public static class ChangeAvatarResponse extends ServiceResponse
    {
        public String avatarUrl;
    }

    public static class UpdateProfileRequest
    {
        public String firstName;
        public String lastName;
        public String phoneNumber;

        public UpdateProfileRequest(String firstName, String lastName, String phoneNumber)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
        }
    }

    public static class UpdateProfileResponse extends  ServiceResponse
    {
        public String firstName;
        public String lastName;
        public String phoneNumber;
    }

    public static class ChangePasswordRequest
    {
        public String currentPassword;
        public String newPassword;
        public String confirmNewPassword;

        public ChangePasswordRequest(String currentPassword, String newPassword, String confirmNewPassword)
        {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
            this.confirmNewPassword = confirmNewPassword;
        }
    }

    public static class ChangePasswordResponse extends ServiceResponse
    {
    }

    public static class AddMoneyRequest
    {
        public float amount;

        public AddMoneyRequest(float amount)
        {
            this.amount = amount;
        }
    }

    public static class AddMoneyResponse extends ServiceResponse
    {
        public float newBalance;
    }

    public static class UserDetailsUpdatedEvent
    {
        public User user;

        public UserDetailsUpdatedEvent(User user)
        {
            this.user = user;
        }
    }
}
