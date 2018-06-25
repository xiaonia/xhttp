package com.xosp.xhttp.builder;

import com.android.volley.Request;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.constant.VolleyConstants;
import com.xosp.xhttp.request.VolleyRequest;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description:
 */
public class PostJsonBuilder<T> extends RequestBuilder<T, PostJsonBuilder<T>> {

    public PostJsonBuilder(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public Request<VolleyResult<T>> build() {
        final VolleyRequest<T> request =
                new VolleyRequest<T>(Request.Method.POST, url, callback);

        request.setContentType(VolleyConstants.CONTENT_TYPE_JSON);
        request.setTag(tag);
        request.setRequestId(id);
        request.setHeaderMap(headers);
        request.setParamMap(params);
        request.setRequestBody(body);
        request.setReturnType(clazz);
        return request;
    }

}
