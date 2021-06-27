package com.carot.sec.context;

import com.carot.sec.enums.OperationTypeEnum;
import lombok.Data;

@Data
public class CSearchContext {

    private OperationTypeEnum operationTypeEnum;

    private Object obj;

}
