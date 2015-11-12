package com.javeshop.javeshop.infrastructure;

import android.util.Log;

import com.javeshop.javeshop.services.ServiceResponse;
import com.squareup.otto.Bus;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class RetrofitCallbackPost<T extends ServiceResponse> extends RetrofitCallback<T>
{
    private static final String TAG = "RetrofitCallbackPost";

    private final Bus bus;

    public RetrofitCallbackPost(Class<T> resultType, Bus bus)
    {
        super(resultType);
        this.bus = bus;
    }

    @Override
    protected void onResponse(T t)
    {
        if (t == null)
        {
            try
            {
                t = resultType.newInstance();
            }
            catch (Exception e)
            {
                Log.e(TAG, "No se puedo crear objeto vacio", e);
            }
        }

        bus.post(t);
    }
}
