package com.carrot.sec.operations.query;

import com.carrot.sec.config.CSearchConfig;
import com.carrot.sec.context.CSearchPipeContext;
import com.carrot.sec.context.JsonSearchContext;
import com.carrot.sec.context.field.CSearchPipeFieldContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.context.query.CSearchPipeQueryContext;
import com.carrot.sec.context.query.JsonQueryFieldContext;
import com.carrot.sec.enums.CFieldPipeTypeEnum;
import com.carrot.sec.enums.OccurEnum;
import com.carrot.sec.enums.OperationTypeEnum;
import com.carrot.sec.handle.HandleInstance;
import com.carrot.sec.interfaces.Handle;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDoc {

    private static final List<Handle> C_FIELD_HANDLES = HandleInstance.getcFieldHandles();

    public Map<String,Object> queryDoc(JsonSearchContext jsc) throws Exception {

        CSearchConfig searchConfig = CSearchConfig.getConfig(jsc.getClassName());

        Directory directory = null;
        DirectoryReader iReader = null;

        try {
            directory = FSDirectory.open(searchConfig.getPath());

            iReader = DirectoryReader.open(directory);

            IndexSearcher indexSearcher = new IndexSearcher(iReader);

            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            List<SortField> sortFields = new ArrayList<>();

            CSearchPipeContext context = new CSearchPipeContext(OperationTypeEnum.QUERY);

            context.setAnalyzer(searchConfig.getAnalyzer());

            List<JsonFieldContext> fieldContexts = jsc.getFieldContexts();

            Map<String,Object> resultWrapper = new HashMap<>(16);

            List<Map<String,Object>> result = new ArrayList<>();

            for(JsonFieldContext jfc : fieldContexts){

                CSearchPipeFieldContext fieldContext = new CSearchPipeFieldContext();

                String fieldName = jfc.getFieldName();
                Object fieldValue = jfc.getFieldValue();

                fieldContext.setFieldName(fieldName);
                fieldContext.setFieldValue(fieldValue);

                JsonQueryFieldContext queryFieldContext = jfc.getJsonQueryFieldContext();
                String queryType = queryFieldContext.getType();
                boolean queryIsSort = queryFieldContext.isSort();
                String occur = queryFieldContext.getOccur();

                CSearchPipeQueryContext queryContext = new CSearchPipeQueryContext(builder);

                queryContext.setEnums(CFieldPipeTypeEnum.getEnumByFlag(queryType));
                queryContext.setSort(queryIsSort);
                if(occur.equals(OccurEnum.Occur.MUST.toString())){
                    queryContext.setOccur(BooleanClause.Occur.MUST);
                }else if(occur.equals(OccurEnum.Occur.FILTER.toString())){
                    queryContext.setOccur(BooleanClause.Occur.FILTER);
                }else if(occur.equals(OccurEnum.Occur.SHOULD.toString())){
                    queryContext.setOccur(BooleanClause.Occur.SHOULD);
                }else if(occur.equals(OccurEnum.Occur.MUST_NOT.toString())){
                    queryContext.setOccur(BooleanClause.Occur.MUST_NOT);
                }else{
                    throw new RuntimeException("not found occur !");
                }

                fieldContext.setQueryContext(queryContext);

                context.addFieldContext(fieldContext);

                for(Handle handle : C_FIELD_HANDLES){
                    if(!handle.support(fieldContext)){
                        continue;
                    }
                    Boolean res = (Boolean)handle.handle(fieldContext);
                    sortFields.addAll(fieldContext.getSortFields());

                }

            }

            Query query = builder.build();

            if(StringUtils.isEmpty(query.toString())){
                query = new MatchAllDocsQuery();
            }

            Integer current = jsc.getCurrent() == null ? 1 : jsc.getCurrent();
            Integer pageSize = jsc.getPageSize() == null ? 10 : jsc.getPageSize();

            ScoreDoc before = null;
            if(current != 1){

                Sort sort = null;
                if(!sortFields.isEmpty()){
                    sort = new Sort(sortFields.toArray(new SortField[]{}));
                }

                TopDocs docsBefore;
                if(sort == null){
                    docsBefore = indexSearcher.search(query, (current-1)*pageSize);
                }else{
                    docsBefore = indexSearcher.search(query, (current-1)*pageSize , sort , true);
                }

                ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
                if(scoreDocs.length > 0){
                    before = scoreDocs[scoreDocs.length - 1];
                }
            }

            Sort sort = null;
            if(!sortFields.isEmpty()){
                sort = new Sort(sortFields.toArray(new SortField[]{}));
            }

            TopDocs docs;
            if(sort == null){
                docs = indexSearcher.searchAfter(before, query, pageSize);
            }else{
                docs = indexSearcher.searchAfter(before, query, pageSize , sort , false);
            }

            ScoreDoc[] scoreDocs = docs.scoreDocs;
//            System.out.println("所有的数据总数为："+docs.totalHits);
//            System.out.println("本页查询到的总数为："+scoreDocs.length);

            for(ScoreDoc hit : scoreDocs){
                Map<String,Object> resMap = new HashMap<>();
                Document hitDoc = indexSearcher.doc(hit.doc);
                List<IndexableField> fields = hitDoc.getFields();
                for(IndexableField idxField : fields){
//                    System.out.println(idxField.name() + " : " + idxField.getCharSequenceValue());
                    resMap.put(idxField.name(),idxField.getCharSequenceValue());
                }
                result.add(resMap);
            }

            resultWrapper.put("data",result);
            resultWrapper.put("pageSize",scoreDocs.length);
            resultWrapper.put("total",docs.totalHits);

            return resultWrapper;
        } finally {
            if(iReader != null){
                try {
                    iReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private TopDocs getScoreDocsByPerPage(int page, int perPage, IndexSearcher searcher, Query query) throws IOException{
        TopDocs result ;

        if(query == null){
            System.out.println(" Query is null return null ");
            return null;
        }

        ScoreDoc before = null;
        if(page != 1){

            SortField sortField = new SortField("id", SortField.Type.LONG,false);
            Sort sort = new Sort(sortField);

            TopDocs docsBefore = searcher.search(query, (page-1)*perPage , sort , true);
            ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
            if(scoreDocs.length > 0){
                before = scoreDocs[scoreDocs.length - 1];
            }
        }

        SortField sortField = new SortField("id", SortField.Type.LONG,false);
        Sort sort = new Sort(sortField);
        result = searcher.searchAfter(before, query, perPage , sort , false);
        return result;
    }

}
