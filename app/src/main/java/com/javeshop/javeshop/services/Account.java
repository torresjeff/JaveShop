package com.javeshop.javeshop.services;

import android.net.Uri;

import com.javeshop.javeshop.infrastructure.User;

/**
 * Contiene clases estaticas que representan los requests/responses que tienen que ver con la cuenta del usuario y la informacion que debe contener cada uno para comunicarse con el servidor.
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

        @Override
        public String toString()
        {
            return "UserResponse{" +
                    "id=" + id +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", balance=" + balance +
                    ", reputation=" + reputation +
                    ", email='" + email + '\'' +
                    ", authToken='" + authToken + '\'' +
                    '}';
        }
    }

    /**
     * Informacion necesaria para inciar sesion con username y password.
     */
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

    /**
     * Respuesta del servidor para LoginWithUsernameRequest.
     */
    public static class LoginWithUsernameResponse extends UserResponse
    {
    }

    /**
     * Informacion necesaria para inciar sesion con un token.
     * El token previene que el usuario tenga que iniciar sesion cada vez que se sale de la application.
     */
    public static class LoginWithLocalTokenRequest
    {
        public String authToken;

        public LoginWithLocalTokenRequest(String authToken)
        {
            this.authToken = authToken;
        }
    }

    /**
     * Respuesta del servidor para LoginWithLocalTokenRequest.
     */
    public static class LoginWithLocalTokenResponse extends UserResponse
    {
    }

    /**
     * Informacion necesaria para que un usuario se pueda registrar a la aplicacion.
     */
    public static class RegisterRequest
    {
        public String firstName;
        public String lastName;
        public String email;
        public String password;
        public String phoneNumber;

        public RegisterRequest(String firstName, String lastName, String email, String password, String phoneNumber)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * Respuesta del servidor a RegisterRequest.
     */
    public static class RegisterResponse extends ServiceResponse
    {
        public String toString()
        {
            return super.toString();
        }
    }

    /**
     * Se manda el nuevo archivo para que el servidor lo guarde como el nuevo avatar del usuario.
     */
    public static class ChangeAvatarRequest
    {
        public Uri newAvatarUri;
        public ChangeAvatarRequest(Uri newAvatarUri)
        {
            this.newAvatarUri = newAvatarUri;
        }
    }

    /**
     * Respuesta del servidor a ChangeAvatarRequest. Responde con el nuevo URL de la imagen para que la interfaz se actualice adecuadamente.
     */
    public static class ChangeAvatarResponse extends ServiceResponse
    {
        public String avatarUrl;
    }

    /**
     * Request para actualizar la informacion personal del usuario (nombre, apellido, telefono)
     */
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

    /**
     * Respuesta del servidor a UpdateProfileRequest.
     */
    public static class UpdateProfileResponse extends  ServiceResponse
    {
        public String firstName;
        public String lastName;
        public String phoneNumber;
    }

    /**
     * Request para cambiar la contrasena de un usuario.
     */
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

    /**
     * Respuesta del servidor a ChangePasswordRequest.
     */
    public static class ChangePasswordResponse extends ServiceResponse
    {
    }

    /**
     * Request para agregar credito a la cuenta de un usuario.
     */
    public static class AddMoneyRequest
    {
        public float amount;

        public AddMoneyRequest(float amount)
        {
            this.amount = amount;
        }
    }

    /**
     * Respuesta del servidor a AddMoneyRequest. Responde con el nuevo balance del usuario.
     */
    public static class AddMoneyResponse extends ServiceResponse
    {
        public float newBalance;
    }

    /**
     * Evento que se dispara cuando el usuario actualiza sus datos personales, para que la interfaz grafica de la aplicacion se ajuste acordemente.
     */
    public static class UserDetailsUpdatedEvent
    {
        public User user;

        public UserDetailsUpdatedEvent(User user)
        {
            this.user = user;
        }
    }
}
