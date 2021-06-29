package com.carrot.sec.context;

import com.carrot.sec.context.field.CSearchPipeFieldContext;
import com.carrot.sec.enums.OperationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.Analyzer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CSearchPipeContext {

    @Setter
    private Analyzer analyzer;

    private final OperationTypeEnum operationTypeEnum;

    private final List<CSearchPipeFieldContext> fieldContexts = new ArrayList<>();

    public CSearchPipeContext(OperationTypeEnum operationTypeEnum) {
        this.operationTypeEnum = operationTypeEnum;
    }

    public void addFieldContext(CSearchPipeFieldContext fieldContext) {
        if (operationTypeEnum.equals(OperationTypeEnum.QUERY)) {
            fieldContext.setAnalyzer(this.analyzer);
        }
        this.fieldContexts.add(fieldContext);
    }


}
