package com.example.samuyu.testtwitter;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by toyamaosamuyu on 2014/11/03.
 */
public enum RequestQueueHelper {
    INSTANCE;

    private static final String TAG = RequestQueueHelper.class.getSimpleName();

    public RequestQueue getRequestQueue(Context context) {
        return Volley.newRequestQueue(context);
    }
}
