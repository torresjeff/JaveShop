package com.javeshop.javeshop.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Clase base para todas las respuestas del servidor.
 * Esta clase contiene la informacion minima con la que el servidor debe responder (descripcion de errores, si el error fue critico, etc)
 */
public class ServiceResponse
{
    private static final String TAG = "ServiceResponse";

    /**
     * Error que ocurre a escals de operacion. Por ejemplo, una imagen no se puede subir al servidor, no hay internet, etc.
     */
    private String operationError;

    /**
     * Errores de validacion. Por ejemplo, el nombre es muy largo, el email no tenia un formato correcto.
     */
    private HashMap<String, String> propertyErrors;

    /**
     * El error es critico. Por ejemplo, no hay internet.
     */
    private boolean isCritical;

    public ServiceResponse()
    {
        propertyErrors = new HashMap<>();
    }

    public ServiceResponse(String operationError)
    {
        this.operationError = operationError;
    }

    public ServiceResponse(String operationError, boolean isCritical)
    {
        this.operationError = operationError;
        this.isCritical = isCritical;
    }

    public String getOperationError()
    {
        return this.operationError;
    }

    public void setOperationError(String operationError)
    {
        this.operationError = operationError;
    }

    public boolean isCritical()
    {
        return isCritical;
    }

    public void setCritical(boolean isCritical)
    {
        this.isCritical = isCritical;
    }

    public void setCriticalError(String criticalError)
    {
        isCritical = true;
        operationError = criticalError;
    }

    public void setPropertyError(String property, String error)
    {
        propertyErrors.put(property, error);
    }

    public String getPropertyError(String property)
    {
        return propertyErrors.get(property);
    }

    public boolean succeeded()
    {
        return (operationError == null || operationError.isEmpty()) && (propertyErrors.size() == 0);
    }

    /**
     * Muestra una notificacion al usuario en caso de que el request que haya hecho no haya sido satisfactorio.
     * @param context
     */
    public void showErrorToast(Context context)
    {
        if (context == null || operationError == null || operationError.isEmpty())
            return;

        try
        {
            Toast.makeText(context, operationError, Toast.LENGTH_LONG).show();;
        }
        catch (Exception e)
        {
            Log.e(TAG, "Can't create error toast", e);
        }
    }
}
