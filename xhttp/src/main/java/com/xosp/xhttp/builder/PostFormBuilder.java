package com.xosp.xhttp.builder;

import com.android.volley.Request;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.constant.VolleyConstants;
import com.xosp.xhttp.utils.VolleyHelper;
import com.xosp.xhttp.request.VolleyRequest;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description:
 */
public class PostFormBuilder<T> extends RequestBuilder<T, PostFormBuilder<T>> {

    public PostFormBuilder(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public Request<VolleyResult<T>> build() {
        String getUrl = VolleyHelper.buildHttpUrl(url, params, VolleyConstants.DEFAULT_CHARSET);
        final VolleyRequest<T> request =
                new VolleyRequest<T>(Request.Method.POST, getUrl, callback);

        request.setContentType(VolleyConstants.CONTENT_TYPE_FORM);
        request.setTag(tag);
        request.setRequestId(id);
        request.setHeaderMap(headers);
        //request.setParamMap(params);
        //request.setRequestBody(body);
        request.setReturnType(clazz);
        return request;
    }

}
