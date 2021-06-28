package com.carrot.sec.handle.query;

import com.carrot.sec.annotation.CFieldQuery;
import com.carrot.sec.context.CSearchPipeContext;
import com.carrot.sec.enums.CFieldTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wty
 */
public class TextFieldQueryHandle implements Handle<CSearchPipeContext, Boolean> {

    @Override
    public Boolean handle(CSearchPipeContext context) {

        Object o = context.getFieldValue();

        Field field = context.getField();

        String name = field.getName();
        CFieldQuery cField = context.getCFieldQuery();
        if(cField.sort()){
            context.addSortField(new SortField(name,cField.enums().getType()));
        }

        if( o != null){

            if(cField.isDate()){
                o = new SimpleDateFormat(cField.dateFormat()).format(((Date)o));
            }

            BooleanQuery.Builder queryBuilder = context.getQueryBuilder();

            QueryParser queryParser = new QueryParser(name, context.getAnalyzer());
            Query query = null;
            try {
                query = queryParser.parse(o.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            queryBuilder.add(query,cField.occur());

            Term term = new Term(name,o.toString());
            context.addTerm(term);

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
        return cField.enums().equals(CFieldTypeEnum.TEXT_FIELD);
    }

}
