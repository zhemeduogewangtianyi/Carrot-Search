package com.carrot.sec.enums;

public enum NewCFieldTypeEnum {

    /***/

    INT_POINT("存储 int 类型" ,"INT"),
    LONG_POINT("存储 long 类型" , "LONG"),
    TEXT_FIELD("存储 string 类型，分词" ,"TEXT"),
    STRING_FIELD("存储 string 类型，不分词" ,"STRING"),

    ;

    private final String desc;
    private final String type;

    NewCFieldTypeEnum(String desc ,String type) {
        this.desc = desc;
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }
}
