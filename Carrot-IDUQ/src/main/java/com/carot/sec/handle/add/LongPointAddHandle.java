package com.carot.sec.handle.add;

import com.carot.sec.annotation.CFieldAdd;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.enums.CFieldTypeEnum;
import com.carot.sec.interfaces.Handle;
import org.apache.lucene.index.IndexableField;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author wty
 */
public class LongPointAddHandle implements Handle<CSearchPipeContext,IndexableField> {

    @Override
    public IndexableField handle(CSearchPipeContext context) {

        Object o = context.getFieldValue();

        if(o != null){

            Field field = context.getField();
            String name = field.getName();
            CFieldAdd cField = context.getCFieldAdd();
            CFieldTypeEnum enums = cField.enums();

            if(cField.isDate()){
                o = ((Date)o).getTime();
            }

            Class<?>[] parameter = {String.class,long.class};
            Object[] args = {name,Long.parseLong(o.toString())};
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
        return cField.enums().equals(CFieldTypeEnum.LONG_POINT);
    }

}
