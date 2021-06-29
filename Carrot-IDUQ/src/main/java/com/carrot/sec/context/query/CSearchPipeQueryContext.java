package com.carrot.sec.context.query;

import com.carrot.sec.enums.CFieldPipeTypeEnum;
import lombok.Data;
import lombok.Setter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;

@Data
public class CSearchPipeQueryContext {

    private CFieldPipeTypeEnum enums;

    private boolean sort;

    private BooleanClause.Occur occur;

    private final BooleanQuery.Builder queryBuilder;

    @Setter
    private Analyzer analyzer;

    public CSearchPipeQueryContext(BooleanQuery.Builder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

}
