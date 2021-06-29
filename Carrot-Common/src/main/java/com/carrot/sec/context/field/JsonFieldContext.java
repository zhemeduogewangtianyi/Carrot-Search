package com.carrot.sec.context.field;

import com.carrot.sec.context.add.JsonAddFieldContext;
import com.carrot.sec.context.query.JsonQueryFieldContext;
import lombok.Data;

@Data
public class JsonFieldContext {

    private String fieldName;
    private Object fieldValue;

    private JsonAddFieldContext jsonAddFieldContext;
    private JsonQueryFieldContext jsonQueryFieldContext;


}
