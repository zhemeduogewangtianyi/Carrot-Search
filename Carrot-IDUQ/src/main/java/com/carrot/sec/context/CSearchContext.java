package com.carrot.sec.context;

import com.carrot.sec.enums.OperationTypeEnum;
import lombok.Data;

@Data
public class CSearchContext {

    private OperationTypeEnum operationTypeEnum;

    private Object obj;

}
