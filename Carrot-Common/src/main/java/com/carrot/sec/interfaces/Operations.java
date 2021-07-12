package com.carrot.sec.interfaces;

import java.util.List;
import java.util.Map;

/**
 * client crud
 * @author WTY
 * @date 2021/7/12 7:55 下午
 */
public interface Operations {

    boolean create(Object obj) throws Throwable;

    boolean delete(Object obj) throws Throwable;

    boolean update(Object obj) throws Throwable;

    List<Map<String, Object>> select(Object obj, Integer current, Integer pageSize) throws Throwable;

}
