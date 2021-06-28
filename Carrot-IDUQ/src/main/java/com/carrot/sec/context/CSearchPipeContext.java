package com.carrot.sec.context;

import com.carrot.sec.annotation.CFieldAdd;
import com.carrot.sec.annotation.CFieldQuery;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.SortField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CSearchPipeContext {

    @Setter
    private String fieldName;

    @Setter
    private Class<?> classType;

    @Setter
    private Object fieldValue;

    @Setter
    private CFieldAdd cFieldAdd;

    @Setter
    private CFieldQuery cFieldQuery;

    private Field field;

    private final Document doc;

    private final BooleanQuery.Builder queryBuilder;

    private final List<SortField> sortFields = new ArrayList<>();

    private final List<Term> terms = new ArrayList<>();

    @Setter
    private Analyzer analyzer;


    public CSearchPipeContext(Document doc) {
        this.doc = doc;
        this.queryBuilder = null;
    }

    public CSearchPipeContext(BooleanQuery.Builder queryBuilder) {
        this.queryBuilder = queryBuilder;
        this.doc = null;
    }

    public void setField(Field field){
        field.setAccessible(true);
        this.field = field;
    }

    public void addSortField(SortField sortField){
        this.sortFields.add(sortField);
    }

    public void addTerm(Term term){
        this.terms.add(term);
    }



}
