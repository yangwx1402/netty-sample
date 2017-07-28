package com.young.netty.example.util;

import java.lang.reflect.Method;

/**
 * Created by yangyong3 on 2017/7/28.
 */
public class MethodCall {
    public static Method getMethod(Class clazz, String method, Class[] argsClass) throws NoSuchMethodException {
        return clazz.getMethod(method,argsClass);
    }
}
