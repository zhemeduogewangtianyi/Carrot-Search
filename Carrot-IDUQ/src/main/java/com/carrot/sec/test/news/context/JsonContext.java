package com.carrot.sec.test.news.context;

import com.carrot.sec.test.news.enums.NewOperationTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class JsonContext {

    @Setter
    private String className;

    private final List<JsonFieldContext> fieldContexts = new ArrayList<>();

    private final Integer operationType;

    public JsonContext(NewOperationTypeEnum operationTypeEnum) {
        this.operationType = operationTypeEnum.getValue();
    }

    public void addFieldContexts(JsonFieldContext fieldContext) {
        this.fieldContexts.add(fieldContext);
    }
}
