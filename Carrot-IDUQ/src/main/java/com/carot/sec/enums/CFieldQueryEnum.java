package com.carot.sec.enums;

public enum CFieldQueryEnum {
    
    QUERY_INT_POINT("query_int"),
    QUERY_LONG_POINT("query_long"),
    QUERY_TEXT_POINT("query_text"),
    QUERY_STRING_POINT("query_string"),
    ;
    
    private String name;

    CFieldQueryEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
