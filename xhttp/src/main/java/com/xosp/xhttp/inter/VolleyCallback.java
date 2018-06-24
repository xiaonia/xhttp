package com.xosp.xhttp.inter;

import com.android.volley.Response;
import com.xosp.xhttp.bean.VolleyResult;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description: Callback interface for delivering parsed responses.
 */
public interface VolleyCallback<T> extends
        Response.Listener<VolleyResult<T>>, Response.ErrorListener {

    //left empty

}
