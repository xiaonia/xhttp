package com.xosp.xhttp.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.constant.VolleyConstants;
import com.xosp.xhttp.inter.VolleyCallback;
import com.xosp.xhttp.utils.ResponseUtils;

import java.io.UnsupportedEncodingException;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description: class for custom network requests.
 */
@SuppressWarnings({"WeakerAccess"})
public class VolleyRequest<T> extends BasicRequest<T> {

    protected String mStringBody;

    public VolleyRequest(int method, String url, VolleyCallback<T> callback) {
        super(method, url, callback);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] bytes = getStringBody();
        return bytes != null? bytes : super.getBody();
    }

    protected byte[] getStringBody() {
        if (mStringBody != null) {
            try {
                return mStringBody.getBytes(VolleyConstants.DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<VolleyResult<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            final Class<T> resultClass = getResultClass();
            if (resultClass == null) {
                return Response.success(new VolleyResult<T>(response, null),
                        HttpHeaderParser.parseCacheHeaders(response));
                //return Response.error(new ParseError(response));
            }

            T result = ResponseUtils.parseNetworkResponse(response, resultClass);
            if (result != null) {
                return Response.success(new VolleyResult<T>(response, result),
                        HttpHeaderParser.parseCacheHeaders(response));
            }

            return Response.error(new ParseError(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (OutOfMemoryError oom) {
            return Response.error(new ParseError(oom));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError == null) {
            volleyError = new VolleyError(new IllegalStateException("Unknown error."));
        }
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    public void setStringBody(String stringBody) {
        this.mStringBody = stringBody;
    }

}
