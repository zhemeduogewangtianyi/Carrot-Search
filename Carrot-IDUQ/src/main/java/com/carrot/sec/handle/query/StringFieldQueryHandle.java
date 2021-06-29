package com.carrot.sec.handle.query;

import com.carrot.sec.context.field.CSearchPipeFieldContext;
import com.carrot.sec.context.query.CSearchPipeQueryContext;
import com.carrot.sec.enums.CFieldPipeTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wty
 */
public class StringFieldQueryHandle implements Handle<CSearchPipeFieldContext, Boolean> {

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
            Term term = new Term(name,fieldValue.toString());
            TermQuery query = new TermQuery(term);
            queryBuilder.add(query,queryContext.getOccur());

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
        return queryContext.getEnums().equals(CFieldPipeTypeEnum.STRING_FIELD);
    }

}
