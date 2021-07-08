package com.carrot.sec.interfaces;


import org.apache.dubbo.common.extension.SPI;

@SPI
public interface Handle<T,R> extends Supported<T>{

    R handle(T context) throws Throwable;

}
