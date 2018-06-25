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
public class GetFormBuilder<T> extends RequestBuilder<T, GetFormBuilder<T>> {

    public GetFormBuilder() {
        super();
    }

    public GetFormBuilder(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public Request<VolleyResult<T>> build() {
        String getUrl = VolleyHelper.buildHttpUrl(url, params, VolleyConstants.DEFAULT_CHARSET);
        final VolleyRequest<T> request =
                new VolleyRequest<T>(Request.Method.GET, getUrl, callback);

        request.setContentType(VolleyConstants.CONTENT_TYPE_FORM);
        request.setTag(tag);
        request.setRequestId(id);
        request.setHeaderMap(headers);
        request.setReturnType(clazz);
        return request;
    }

}
