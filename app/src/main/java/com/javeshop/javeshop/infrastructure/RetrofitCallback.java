package com.javeshop.javeshop.infrastructure;

import android.util.Log;

import com.javeshop.javeshop.services.ServiceResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public abstract class RetrofitCallback<T extends ServiceResponse> implements Callback<T>
{
    private static final String TAG = "RETROFIT_CALLBACK";

    protected final Class<T> resultType; //Is the template itself. Used in order to instantiate responses when a failure happens

    public RetrofitCallback(Class<T> resultType)
    {
        this.resultType = resultType;
    }

    protected abstract void onResponse (T t);

    @Override
    public void success(T t, Response response)
    {
        onResponse(t);
    }

    @Override
    public void failure(RetrofitError error)
    {
        Log.e(TAG, "Error sending request with " + resultType.getSimpleName() + " response", error);

        ServiceResponse errorResult;
        try
        {
            errorResult = resultType.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error creating result type " + resultType.getName());
        }

        if (error.getKind() == RetrofitError.Kind.NETWORK)
        {
            errorResult.setCriticalError("No nos pudimos conectar a los servidores de JaveShop");
            onResponse((T)errorResult);
            return;
        }

        if (error.getSuccessType() == null)
        {
            errorResult.setCriticalError("Error desconocido. Por favor intenta de nuevo.");
            onResponse((T) errorResult);
            return;
        }

        try
        {
            if (error.getBody() instanceof ServiceResponse)
            {
                ServiceResponse result = (ServiceResponse) error.getBody();

                if (result.succeeded())
                {
                    result.setCriticalError("Error desconocido. Por favor intenta de nuevo.");
                }

                onResponse((T) result);
            }
            else
            {
                throw new RuntimeException("Result class " + resultType.getName() + " does not extend ServiceResponse.");
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Unknown error", e);
            errorResult.setCriticalError("Error desconocido. Por favor intenta de nuevo.");
            onResponse((T) errorResult);
        }
    }
}
