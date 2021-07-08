package com.carrot.sec.enums;

public enum NewOperationTypeEnum {

    ADD("add",0),
    DELETE("delete",1),
    UPDATE("update",2),
    QUERY("query",3),

    ;

    private final String key;
    private final Integer value;

    NewOperationTypeEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    public static NewOperationTypeEnum getByValue(Integer value){
        for(NewOperationTypeEnum typeEnum : NewOperationTypeEnum.values()){
            if(typeEnum.getValue().equals(value)){
                return typeEnum;
            }
        }
        return null;
    }
}
