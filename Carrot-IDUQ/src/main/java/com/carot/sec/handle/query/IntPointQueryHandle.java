package com.carot.sec.handle.query;

import com.carot.sec.annotation.CFieldQuery;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.enums.CFieldTypeEnum;
import com.carot.sec.interfaces.Handle;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;

import java.lang.reflect.Field;

/**
 * @author wty
 */
public class IntPointQueryHandle implements Handle<CSearchPipeContext, Boolean> {

    @Override
    public Boolean handle(CSearchPipeContext context) {

        Object o = context.getFieldValue();

        Field field = context.getField();
        String name = field.getName();
        CFieldQuery cField = context.getCFieldQuery();
        if(cField.sort()){
            context.addSortField(new SortField(name,cField.enums().getType()));
        }

        if(o != null){

            if(cField.isDate()){
                throw new RuntimeException("int not case Date !");
            }

            BooleanQuery.Builder queryBuilder = context.getQueryBuilder();
            Query query = IntPoint.newExactQuery(name, Integer.parseInt(o.toString()));
            queryBuilder.add(query, cField.occur());
            return true;
        }
        return false;
    }

    @Override
    public boolean support(CSearchPipeContext context) {
        CFieldQuery cField = context.getCFieldQuery();
        if(cField == null){
            return false;
        }
        return cField.enums().equals(CFieldTypeEnum.INT_POINT);
    }

}
