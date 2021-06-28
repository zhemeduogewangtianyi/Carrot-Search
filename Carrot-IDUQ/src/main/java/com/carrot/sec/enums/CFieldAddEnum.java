package com.carrot.sec.enums;

public enum CFieldAddEnum {

    ADD_INT_POINT("add_int"),
    ADD_LONG_POINT("add_long"),
    ADD_TEXT_POINT("add_text"),
    ADD_STRING_POINT("add_string"),
    ADD_STORE_POINT("add_store"),
    ;

    private String name;

    CFieldAddEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
