package com.carrot.sec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryHandleResult {

    private Query query;

    private Term term;

}
