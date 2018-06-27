package com.xosp.xhttp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.google.common.net.MediaType;
import com.xosp.xhttp.inter.MimeType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/26
 * Description:
 */
public class ResponseUtils {

    @SuppressWarnings("unchecked")
    public static <T> T parseNetworkResponse (
            NetworkResponse response, Class<T> resultClass) throws IOException, OutOfMemoryError {

        if (response == null || resultClass == null) {
            return null;
        }

        Map<String, List<String>> headerMap = HeaderUtils.convertHeaders(response.allHeaders);
        //parse response media type
        final MediaType mediaType = HeaderUtils.parseMediaType(headerMap);
        final String media = mediaType == null? null : mediaType.type();

        //parse response result
        T result = null;
        if (Bitmap.class.equals(resultClass)) {
            if (MimeType.IMAGE_TYPE.equals(media)) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        response.data, 0, response.data.length);
                if (bitmap != null) {
                    result = (T) bitmap;
                }
            }
        }
        else {
            String charset = HeaderUtils.parseCharset(headerMap);
            String jsonString = new String(response.data, charset);
            if (String.class.equals(resultClass)
                    || Object.class.equals(resultClass)) { //Object.class 默认返回String
                result = (T) jsonString;
            }
            else if (JSONObject.class.equals(resultClass)) {
                JSONObject jsonObject = JSON.parseObject(jsonString);
                if (jsonObject != null) {
                    result = (T) jsonObject;
                }
            }
            else {
                result = JSON.parseObject(jsonString, resultClass);
            }
        }

        return result;
    }

}
