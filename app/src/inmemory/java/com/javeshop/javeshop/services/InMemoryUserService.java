package com.javeshop.javeshop.services;

import com.javeshop.javeshop.infrastructure.JaveShopApplication;
import com.javeshop.javeshop.services.entities.UserDetails;
import com.squareup.otto.Subscribe;

import java.util.Random;

/**
 * Emula las respuestas del servidor que tienen que ver con la informacion publica de los otros usuarios (diferente del que tiene la sesion iniciada) de la aplicacion.
 */
public class InMemoryUserService extends BaseInMemoryService
{
    public InMemoryUserService(JaveShopApplication application)
    {
        super(application);
    }

    /**
     * Envia un request para cargar los datos publicos del vendedor de un producto.
     * Se envia un evento que emula la respuesta del servidor.
     * @param request request del usuario. Evento que dispara la llamada a esta funcion.
     */
    @Subscribe
    public void getUserDetails(Users.GetUserDetailsRequest request)
    {
        Users.GetUserDetailsResponse response = new Users.GetUserDetailsResponse();
        Random random = new Random();

        response.userDetails = new UserDetails(request.userId,
                "Vendedor",
                Integer.toString(request.userId),
                "http://www.gravatar.com/avatar/" + Integer.toString(random.nextInt()) + "?d=identicon&s=600",
                5.0f);

        postDelayed(response);
    }
}
