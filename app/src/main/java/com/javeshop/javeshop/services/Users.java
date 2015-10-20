package com.javeshop.javeshop.services;

import com.javeshop.javeshop.services.entities.UserDetails;

/**
 * Contiene clases estaticas que representan los requests/responses que tienen que ver con los usuarios y la informacion que debe contener cada uno para comunicarse con el servidor.
 */
public class Users
{
    private Users()
    {
    }

    /**
     * Request para ver la informacion publica de un vendedor.
     */
    public static class GetUserDetailsRequest
    {
        public int userId;
        public GetUserDetailsRequest(int userId)
        {
            this.userId = userId;
        }
    }

    /**
     * Respuesta del servidor a GetUserDetailsRequest. Responde con la informacion publica de un usuario.
     */
    public static class GetUserDetailsResponse extends ServiceResponse
    {
        public UserDetails userDetails;
    }
}
