package com.carrot.sec.operations.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

public class Test {

    public static void main(String[] args) {
        System.out.println(Arrays.asList("5", "6").toString());
        TreeMap<Object, Object> objectObjectTreeMap = new TreeMap<>(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                System.out.println(o1 + " " + o2);
                return 0;
            }
        });

        objectObjectTreeMap.put("a",1);
    }

}
