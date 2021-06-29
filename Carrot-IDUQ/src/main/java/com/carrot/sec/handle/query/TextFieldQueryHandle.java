package com.carrot.sec.handle.query;

import com.carrot.sec.context.field.CSearchPipeFieldContext;
import com.carrot.sec.context.query.CSearchPipeQueryContext;
import com.carrot.sec.enums.CFieldPipeTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wty
 */
public class TextFieldQueryHandle implements Handle<CSearchPipeFieldContext, Boolean> {

    @Override
    public Boolean handle(CSearchPipeFieldContext context) {

        CSearchPipeQueryContext queryContext = context.getQueryContext();

        Object fieldValue = context.getFieldValue();

        String name = context.getFieldName();
        if(queryContext.isSort()){
            context.addSortField(new SortField(name,queryContext.getEnums().getType()));
        }

        if( fieldValue != null){

            BooleanQuery.Builder queryBuilder = context.getQueryContext().getQueryBuilder();

            QueryParser queryParser = new QueryParser(name, context.getAnalyzer());
            Query query = null;
            try {
                query = queryParser.parse(fieldValue.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            queryBuilder.add(query,queryContext.getOccur());

            Term term = new Term(name,fieldValue.toString());
            context.addTerm(term);

            return true;
        }
        return false;
    }

    @Override
    public boolean support(CSearchPipeFieldContext context) {
        if(context == null){
            return false;
        }
        CSearchPipeQueryContext queryContext = context.getQueryContext();
        if(queryContext == null){
            return false;
        }
        return queryContext.getEnums().equals(CFieldPipeTypeEnum.TEXT_FIELD);
    }

}
