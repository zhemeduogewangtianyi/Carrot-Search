package com.carrot.sec.handle.add;

import com.carrot.sec.annotation.CFieldAdd;
import com.carrot.sec.context.CSearchPipeContext;
import com.carrot.sec.enums.CFieldTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.index.IndexableField;

import java.lang.reflect.Field;

/**
 * @author wty
 */
public class IntPointAddHandle implements Handle<CSearchPipeContext, IndexableField> {

    @Override
    public IndexableField handle(CSearchPipeContext context) {

        Object o = context.getFieldValue();

        if(o != null){

            Field field = context.getField();
            String name = field.getName();
            CFieldAdd cField = context.getCFieldAdd();
            CFieldTypeEnum enums = cField.enums();


            if(cField.isDate()){
                throw new RuntimeException("int not case Date !");
            }

            Class<?>[] parameter = {String.class,int[].class};
            Object[] args = {name,new int[]{Integer.parseInt(o.toString())}};
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
        return cField.enums().equals(CFieldTypeEnum.INT_POINT);
    }

}
