package com.sug.core.platform.web.rest.view;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Greg on 2014/10/19.
 * S: 代表源数据类型
 * T: 生成的View Object类型
 * 主要意图是view 输出时过滤掉不需要的字段
 */
public class BaseViewBuilder<S,T> {

    //获取泛型S
    private Class<?> source;

    //获取泛型T
    private Class<?> target;

    public BaseViewBuilder(){
        Type t = this.getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.source = ((Class<?>) p[0]);
            this.target = ((Class<?>) p[1]);
        }
    }

    public T Create(S t) throws Exception {
        Object obj = this.target.newInstance() ;
        Method[] methods = this.target.getMethods();
        for(Method method : methods){
            if(isSetter(method)){
                String methodName = method.getName().replace("set","get");

                if(!hasMethodInSource(this.source.getMethods(), methodName))
                    continue;
                Method getMethod = this.source.getMethod(methodName);
                if(getMethod != null)
                    method.invoke(obj, getMethod.invoke(t));
            }
        }
        return (T) obj;
    }

    public static boolean hasMethodInSource(Method[] methods, String name){
        for (Method m: methods){
            if(name.equals(m.getName()))
                return true;
        }

        return false;
    }

    public static boolean isSetter(Method method){
        if(!method.getName().startsWith("set")) return false;
        if(method.getParameterTypes().length != 1) return false;
        return true;
    }
}
