package com.xosp.xhttp.utils;

import android.support.annotation.Nullable;

import com.xosp.xhttp.inter.VolleyCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/25
 * Description:
 */
public class GenericClassHelper {

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> Class<T> getGenericClass(Object object, Class clazz) {
        return getGenericClass(object, clazz, 0);
    }

    //如果object是匿名内部类，则可以通过此方法获取泛型参数
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    @Nullable
    public static <T> Class<T> getGenericClass(Object object, Class clazz, int index) {
        if (clazz == null && object != null) {
            try {
                Type genericClass = object.getClass().getGenericSuperclass();
                if (genericClass instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType) genericClass;
                    Type rawType = paramType.getRawType();
                    if (rawType instanceof Class
                            && VolleyCallback.class.isAssignableFrom((Class<?>) rawType)) {

                        Type[] types = paramType.getActualTypeArguments();
                        if (types != null && types.length > index
                                && types[index] instanceof Class) {
                            return  (Class<T>) types[index];
                        }
                    }
                }

                Type[] interfaces = object.getClass().getGenericInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    for (Type genericInter : interfaces) {
                        if (genericInter instanceof ParameterizedType) {
                            ParameterizedType paramType = (ParameterizedType) genericInter;
                            Type rawType = paramType.getRawType();
                            if (rawType instanceof Class
                                    && VolleyCallback.class.isAssignableFrom((Class<?>) rawType)) {

                                Type[] types = paramType.getActualTypeArguments();
                                if (types != null && types.length > index
                                        && types[index] instanceof Class) {

                                    return (Class<T>) types[index];
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
