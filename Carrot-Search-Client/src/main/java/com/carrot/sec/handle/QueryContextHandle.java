package com.carrot.sec.handle;

import com.carrot.sec.annotation.CSearchQuery;
import com.carrot.sec.context.OperationHandleContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.context.query.JsonQueryFieldContext;
import com.carrot.sec.enums.NewCFieldTypeEnum;
import com.carrot.sec.enums.NewOperationTypeEnum;
import com.carrot.sec.enums.OccurEnum;
import com.carrot.sec.interfaces.Handle;

import java.lang.reflect.Field;

public class QueryContextHandle implements Handle<OperationHandleContext, JsonFieldContext> {

    @Override
    public JsonFieldContext handle(OperationHandleContext context) throws IllegalAccessException {

        Field declaredField = context.getField();

        Object obj = context.getObj();

        JsonFieldContext fieldContext = new JsonFieldContext();

        String fieldName = declaredField.getName();
        fieldContext.setFieldName(fieldName);

        Object fieldValue = declaredField.get(obj);
        fieldContext.setFieldValue(fieldValue);

        CSearchQuery cSearchQuery = declaredField.getAnnotation(CSearchQuery.class);

        JsonQueryFieldContext queryFieldContext = new JsonQueryFieldContext();

        //condition
        OccurEnum.Occur occur = cSearchQuery.occur();

        String occurStr = occur.toString();
        queryFieldContext.setOccur(occurStr);

        NewCFieldTypeEnum queryTypeEnums = cSearchQuery.enums();
        String queryType = queryTypeEnums.getType();
        queryFieldContext.setType(queryType);

        boolean sort = cSearchQuery.sort();
        queryFieldContext.setSort(sort);

        fieldContext.setJsonQueryFieldContext(queryFieldContext);

        return fieldContext;
    }

    @Override
    public boolean support(OperationHandleContext context) {
        NewOperationTypeEnum operationTypeEnum = context.getOperationTypeEnum();
        if (operationTypeEnum == null) {
            return false;
        }
        boolean query = operationTypeEnum.equals(NewOperationTypeEnum.QUERY);
        boolean delete = operationTypeEnum.equals(NewOperationTypeEnum.DELETE);

        return query || delete;
    }

}
