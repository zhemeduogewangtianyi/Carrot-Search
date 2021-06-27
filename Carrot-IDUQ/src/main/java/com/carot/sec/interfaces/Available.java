package com.carot.sec.interfaces;

public interface Available<T> {

    default boolean available(T t){
        return true;
    }

}
