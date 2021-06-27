package com.carot.sec.context;

import com.carot.sec.annotation.CFieldAdd;
import com.carot.sec.annotation.CFieldQuery;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;

import java.lang.reflect.Field;

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
}
