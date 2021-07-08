package com.carrot.sec.handle;

import com.carrot.sec.annotation.CSearchAdd;
import com.carrot.sec.context.OperationHandleContext;
import com.carrot.sec.context.add.JsonAddFieldContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.enums.NewCFieldTypeEnum;
import com.carrot.sec.enums.NewOperationTypeEnum;
import com.carrot.sec.interfaces.Handle;

import java.lang.reflect.Field;

public class AddContextHandle implements Handle<OperationHandleContext, JsonFieldContext> {

    @Override
    public JsonFieldContext handle(OperationHandleContext context) throws Exception {

        Field declaredField = context.getField();
        Object obj = context.getObj();

        JsonFieldContext fieldContext = new JsonFieldContext();

        String fieldName = declaredField.getName();
        fieldContext.setFieldName(fieldName);

        Object fieldValue = declaredField.get(obj);
        fieldContext.setFieldValue(fieldValue);

        CSearchAdd cSearchAdd = declaredField.getAnnotation(CSearchAdd.class);

        JsonAddFieldContext addFieldContext = new JsonAddFieldContext();

        //is need analyzer
        boolean analyzer = cSearchAdd.analyzer();
        addFieldContext.setAnalyzer(analyzer);

        //lucene process type
        NewCFieldTypeEnum addTypeEnum = cSearchAdd.enums();
        String addType = addTypeEnum.getType();
        addFieldContext.setType(addType);

        //need store
        boolean store = cSearchAdd.store();
        addFieldContext.setStore(store);

        //add context
        fieldContext.setJsonAddFieldContext(addFieldContext);

        return fieldContext;

    }

    @Override
    public boolean support(OperationHandleContext context) {
        NewOperationTypeEnum operationTypeEnum = context.getOperationTypeEnum();
        if(operationTypeEnum == null){
            return false;
        }
        return operationTypeEnum.equals(NewOperationTypeEnum.ADD);
    }

}
