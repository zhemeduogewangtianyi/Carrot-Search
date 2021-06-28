package com.carrot.sec.test.news.context;

import lombok.Data;

@Data
public class JsonFieldContext {

    private String fieldName;
    private Object fieldValue;

    private JsonAddFieldContext jsonAddFieldContext;
    private JsonQueryFieldContext jsonQueryFIeldContext;


}
