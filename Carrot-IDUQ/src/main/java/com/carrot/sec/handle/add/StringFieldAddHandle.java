package com.carrot.sec.handle.add;

import com.carrot.sec.context.add.CSearchPipeAddContext;
import com.carrot.sec.context.field.CSearchPipeFieldContext;
import com.carrot.sec.enums.CFieldPipeTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexableField;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wty
 */
public class StringFieldAddHandle implements Handle<CSearchPipeFieldContext, IndexableField> {

    @Override
    public IndexableField handle(CSearchPipeFieldContext context) {

        CSearchPipeAddContext addContext = context.getAddContext();

        Object fieldValue = context.getFieldValue();

        if( fieldValue != null){

            String name = context.getFieldName();
            CFieldPipeTypeEnum enums = addContext.getEnums();

            Class<?>[] parameter = {String.class,String.class, Field.Store.class};
            Object[] args = {name,fieldValue.toString(),addContext.isStore() ? Field.Store.YES : Field.Store.YES};

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
        return addContext.getEnums().equals(CFieldPipeTypeEnum.STRING_FIELD);
    }

}
