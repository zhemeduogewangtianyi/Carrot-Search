package com.carrot.sec.enums;

public enum OperationTypeEnum {

    ADD("add",0),
    DELETE("delete",1),
    UPDATE("update",2),
    QUERY("query",3),

    ;

    private final String key;
    private final Integer value;

    OperationTypeEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}
