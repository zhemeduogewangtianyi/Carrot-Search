package com.carrot.sec.interfaces;

/**
 * @author wty
 */
public interface Supported<T> {

    default boolean support(T t){
        return false;
    }

}
