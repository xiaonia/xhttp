package com.xosp.xhttp.core;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ByteArrayPool;
import com.android.volley.toolbox.HttpStack;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description:
 */
public class BasicNetworkCompat extends BasicNetwork {

    private static final String TAG = "BasicNetworkCompat";

    public BasicNetworkCompat(HttpStack httpStack) {
        super(httpStack);
    }

    public BasicNetworkCompat(HttpStack httpStack, ByteArrayPool pool) {
        super(httpStack, pool);
    }

    public BasicNetworkCompat(BaseHttpStack httpStack) {
        super(httpStack);
    }

    public BasicNetworkCompat(BaseHttpStack httpStack, ByteArrayPool pool) {
        super(httpStack, pool);
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        //
        return super.performRequest(request);
    }

}
