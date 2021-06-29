package com.carrot.sec.handle.add;

import com.carrot.sec.context.add.CSearchPipeAddContext;
import com.carrot.sec.context.field.CSearchPipeFieldContext;
import com.carrot.sec.enums.CFieldPipeTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.index.IndexableField;

import java.util.Date;

/**
 * @author wty
 */
public class LongPointAddHandle implements Handle<CSearchPipeFieldContext,IndexableField> {

    @Override
    public IndexableField handle(CSearchPipeFieldContext context) {

        CSearchPipeAddContext addContext = context.getAddContext();

        Object fieldValue = context.getFieldValue();

        if(fieldValue != null){

            String name = context.getFieldName();
            CFieldPipeTypeEnum enums = addContext.getEnums();

//            if(addContext.isDate()){
//                fieldValue = new Date(Long.parseLong(fieldValue.toString())).getTime();
//            }

            Class<?>[] parameter = {String.class,long.class};
            Object[] args = {name,Long.parseLong(fieldValue.toString())};
            return enums.getClsField(parameter,args);
        }
        return null;
    }

    @Override
    public boolean support(CSearchPipeFieldContext context) {
        if(context == null){
            return false;
        }
        CSearchPipeAddContext addContext = context.getAddContext();
        if(addContext == null){
            return false;
        }
        return addContext.getEnums().equals(CFieldPipeTypeEnum.LONG_POINT);
    }

}
