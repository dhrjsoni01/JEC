package com.example.dks.jec;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by DKS on 3/22/2017.
 */

public class MySingletonVolly {

    private static MySingletonVolly mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingletonVolly(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }


    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingletonVolly getInstance(Context context){
        if (mInstance==null){
            mInstance = new MySingletonVolly(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }
}
