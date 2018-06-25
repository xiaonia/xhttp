package com.xosp.xhttp.utils;

import android.support.annotation.Nullable;

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
    //递归查找
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    @Nullable
    public static <T> Class<T> getGenericClass(Object object, Class genericClazz, int index) {
        Class<T> result = null;
        if (genericClazz != null && object != null) {
            Class targetClazz = object.getClass();
            while (true) {
                if (targetClazz == null || !genericClazz.isAssignableFrom(targetClazz)) {
                    break;
                }

                result = getGenericClass(targetClazz, genericClazz, index);
                if (result != null) {
                    return result;
                }

                targetClazz = targetClazz.getSuperclass();
            }
        }

        return result;
    }

    //如果object是匿名内部类，则可以通过此方法获取泛型参数
    @SuppressWarnings({"unchecked", "WeakerAccess"})
    @Nullable
    public static <T> Class<T> getGenericClass(final Class target, final Class generic, final int index) {
        if (target != null && generic != null && generic.isAssignableFrom(target)) {
            try {
                Type genericClass = target.getGenericSuperclass();
                if (genericClass instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType) genericClass;
                    Type rawType = paramType.getRawType();
                    if (rawType instanceof Class
                            && generic.isAssignableFrom((Class<?>) rawType)) {

                        Type[] types = paramType.getActualTypeArguments();
                        if (types != null && types.length > index
                                && types[index] instanceof Class) {
                            return  (Class<T>) types[index];
                        }
                    }
                }

                Type[] interfaces = target.getGenericInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    for (Type genericInter : interfaces) {
                        if (genericInter instanceof ParameterizedType) {
                            ParameterizedType paramType = (ParameterizedType) genericInter;
                            Type rawType = paramType.getRawType();
                            if (rawType instanceof Class
                                    && generic.isAssignableFrom((Class<?>) rawType)) {

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
