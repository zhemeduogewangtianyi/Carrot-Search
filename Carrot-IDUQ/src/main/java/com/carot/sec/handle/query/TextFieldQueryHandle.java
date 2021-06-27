package com.carot.sec.handle.query;

import com.carot.sec.annotation.CFieldAdd;
import com.carot.sec.annotation.CFieldQuery;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.enums.CFieldTypeEnum;
import com.carot.sec.interfaces.Handle;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;

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

        if( o != null){

            Field field = context.getField();

            String name = field.getName();
            CFieldQuery cField = context.getCFieldQuery();

            if(cField.isDate()){
                o = new SimpleDateFormat(cField.dateFormat()).format(((Date)o));
            }

            BooleanQuery.Builder queryBuilder = context.getQueryBuilder();

//            Term term = new Term(name,o.toString());
//            FuzzyQuery query = new FuzzyQuery(term);

            QueryParser queryParser = new QueryParser(name, context.getAnalyzer());
            Query query = null;
            try {
                query = queryParser.parse(o.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            queryBuilder.add(query,cField.occur());


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
