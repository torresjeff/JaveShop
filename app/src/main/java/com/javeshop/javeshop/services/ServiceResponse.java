package com.javeshop.javeshop.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class ServiceResponse
{
    private static final String TAG = "ServiceResponse";

    private String operationError; //Error que ocurre a escal de operacion. Por ejemplo, una imagen no se puede subir al servidor, no hay internet, etc.
    private HashMap<String, String> propertyErrors;  //Errores de validacion. Por ejemplo, el nombre es muy largo, el email no tenia un formato correcto
    private boolean isCritical; //El error es critico. Por ejemplo, no hay internet.

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
