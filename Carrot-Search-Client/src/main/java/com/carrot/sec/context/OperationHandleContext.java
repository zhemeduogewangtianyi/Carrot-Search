package com.carrot.sec.context;

import com.carrot.sec.enums.NewOperationTypeEnum;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class OperationHandleContext {
    
    private final Field field;

    private final NewOperationTypeEnum operationTypeEnum;

    private final Object obj;
    
}
