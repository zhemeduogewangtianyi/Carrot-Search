package com.carrot.sec.context.field;

import com.carrot.sec.context.add.CSearchPipeAddContext;
import com.carrot.sec.context.query.CSearchPipeQueryContext;
import com.carrot.sec.enums.OperationTypeEnum;
import lombok.Data;
import lombok.Setter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.SortField;

import java.util.ArrayList;
import java.util.List;

@Data
public class CSearchPipeFieldContext {

    private String fieldName;

    private Object fieldValue;

    @Setter
    private Analyzer analyzer;

    @Setter
    private CSearchPipeAddContext addContext;

    @Setter
    private CSearchPipeQueryContext queryContext;

    private final List<SortField> sortFields = new ArrayList<>();

    private final List<Term> terms = new ArrayList<>();

    public void addSortField(SortField sortField){
        this.sortFields.add(sortField);
    }

    public void addTerm(Term term){
        this.terms.add(term);
    }

}
