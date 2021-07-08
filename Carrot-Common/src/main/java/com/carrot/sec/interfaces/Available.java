package com.carrot.sec.interfaces;

public interface Available<T> {

    default boolean available(T t){
        return true;
    }

}
