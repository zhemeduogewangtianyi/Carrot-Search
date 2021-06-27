package com.carot.sec.interfaces;

import org.apache.dubbo.common.extension.SPI;

import java.util.ArrayDeque;
import java.util.Deque;

@SPI
public interface Interceptor<T,R> extends Supported<T> , Available<T> ,Ordered{

    ThreadLocal<Deque<Object>> tl = new ThreadLocal<Deque<Object>>(){
        @Override
        protected Deque<Object> initialValue() {
            return new ArrayDeque<>();
        }
    };

    Object pre(Handle<T,R> handle , T t) throws Throwable;

    R post(Handle<T,R> handle , T t , Object result) throws Throwable;

    R after(Handle<T,R> handle , T t , Throwable e) throws Throwable;

    R hook(Handle<T,R> handle , T t) throws Throwable;

    @Override
    int order();

}
