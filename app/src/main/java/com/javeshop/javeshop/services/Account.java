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

    public static class LoginWithUsernameResponse extends ServiceResponse
    {
    }

    //El token previene que el usuario tenga que loggearse cada vez que se sale de la aplication.
    public static class LoginWithLocalTokenRequest
    {
        public String authToken;
        public String clientId;

        public LoginWithLocalTokenRequest(String authToken)
        {
            this.authToken = authToken;
            clientId = "android";
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

    public static class RegisterResponse extends UserResponse
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

        public UpdateProfileRequest(String firstName, String lastName)
        {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    public static class UpdateProfileResponse extends  ServiceResponse
    {
        public String firstName;
        public String lastName;
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

    public static class UserDetailsUpdatedEvent
    {
        public User user;

        public UserDetailsUpdatedEvent(User user)
        {
            this.user = user;
        }
    }
}
