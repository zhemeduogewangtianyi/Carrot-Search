package com.carrot.sec.context;

import com.carrot.sec.context.field.JsonFieldContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class JsonSearchContext {

    @Setter
    private String className;

    @Setter
    private Integer current;

    @Setter
    private Integer pageSize;

    @Setter
    private JsonSearchContext targetJsonSearchContext;

    private final List<JsonFieldContext> fieldContexts = new ArrayList<>();

    private Integer operationType;

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public void addFieldContexts(JsonFieldContext fieldContext) {
        this.fieldContexts.add(fieldContext);
    }
}
