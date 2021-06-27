package com.carot.sec.execute.base;

import com.carot.sec.interfaces.Handle;
import com.carot.sec.interfaces.Interceptor;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BaseExecute<T,R> {

    private final List<Interceptor<T,R>> INTERCEPTORS = new ArrayList<>();

    private static final String[] names = {};

    private int index = -1;

    {
        ExtensionLoader<Interceptor> extensionLoader = ExtensionLoader.getExtensionLoader(Interceptor.class);
        for(String name : names){
            INTERCEPTORS.add(extensionLoader.getExtension(name));
        }
        INTERCEPTORS.sort(new Comparator<Interceptor>() {
            @Override
            public int compare(Interceptor o1, Interceptor o2) {
                return o1.order() - o2.order();
            }
        });
    }

    protected Object applyPre(Handle<T,R> handle , T t) throws Throwable {
        for(index = 0 ; index < INTERCEPTORS.size() ; index++){
            Interceptor<T,R> cFieldInterceptor = INTERCEPTORS.get(index);
            if(condition(cFieldInterceptor,t)){
                return cFieldInterceptor.pre(handle,t);
            }

        }
        return null;
    }

    protected void applyPost(Handle<T,R> handle , T t , Object result) throws Throwable {
        for(int i = INTERCEPTORS.size() - 1 ; i >= 0 ; i-- ){
            Interceptor<T,R> interceptor = INTERCEPTORS.get(i);
            if(condition(interceptor,t)){
                interceptor.hook(handle , t);
                interceptor.post(handle,t,result);
            }
        }
    }

    protected void applyAfter(Handle<T,R> handle , T t , Throwable e) {
        for(int i = index ; i >= 0 ; i--){
            Interceptor<T,R> interceptor = INTERCEPTORS.get(i);
            if(condition(interceptor,t)){
                try {
                    interceptor.after(handle,t,e);
                } catch (Throwable throwable) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean condition(Interceptor<T,R> interceptor , T t) {
        if(!interceptor.available(t)){
            return false;
        }
        if(!interceptor.support(t)){
            return false;
        }
        return true;
    }

}
