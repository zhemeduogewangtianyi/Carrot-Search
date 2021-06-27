package com.carot.sec.handle.add;

import com.carot.sec.annotation.CFieldAdd;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.enums.CFieldTypeEnum;
import com.carot.sec.interfaces.Handle;
import org.apache.lucene.index.IndexableField;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wty
 */
public class TextFieldAddHandle implements Handle<CSearchPipeContext, IndexableField> {

    @Override
    public IndexableField handle(CSearchPipeContext context) {

        Object o = context.getFieldValue();

        if( o != null){

            Field field = context.getField();
            String name = field.getName();
            CFieldAdd cField = context.getCFieldAdd();
            CFieldTypeEnum enums = cField.enums();

            if(cField.isDate()){
                o = new SimpleDateFormat(cField.dateFormat()).format(((Date)o));
            }

            Class<?>[] parameter = {String.class,String.class, org.apache.lucene.document.Field.Store.class};
            Object[] args = {name,o.toString(),cField.store() ? org.apache.lucene.document.Field.Store.YES : org.apache.lucene.document.Field.Store.YES};

            return enums.getClsField(parameter,args);
        }
        return null;
    }

    @Override
    public boolean support(CSearchPipeContext context) {
        CFieldAdd cField = context.getCFieldAdd();
        if(cField == null){
            return false;
        }
        return cField.enums().equals(CFieldTypeEnum.TEXT_FIELD);
    }

}
