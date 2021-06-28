package com.carrot.sec.handle.add;

import com.carrot.sec.annotation.CFieldAdd;
import com.carrot.sec.context.CSearchPipeContext;
import com.carrot.sec.enums.CFieldTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.index.IndexableField;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author wty
 */
public class StoreFieldAddHandle implements Handle<CSearchPipeContext, IndexableField> {

    @Override
    public IndexableField handle(CSearchPipeContext context) {

        Object o = context.getFieldValue();

        if( o != null){

            Field field = context.getField();
            String name = field.getName();
            CFieldAdd cField = context.getCFieldAdd();
            CFieldTypeEnum enums = CFieldTypeEnum.STORED_FIELD;

            if(cField.isDate()){
                o = ((Date)o).getTime();
            }

            Class<?>[] parameter = {String.class,String.class};
            Object[] args = {name,o.toString()};

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
        if(cField.enums().equals(CFieldTypeEnum.TEXT_FIELD)){
            return false;
        }
        if(cField.enums().equals(CFieldTypeEnum.STRING_FIELD)){
            return false;
        }
        return cField.store();
    }

}
