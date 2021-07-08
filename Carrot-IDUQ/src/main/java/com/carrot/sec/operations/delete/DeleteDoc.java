package com.carrot.sec.operations.delete;

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
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteDoc {

    private static final List<Handle> C_FIELD_HANDLES = HandleInstance.getcFieldHandles();

    public boolean deleteDoc(JsonSearchContext jsc) {

        CSearchConfig searchConfig = CSearchConfig.getConfig(jsc.getClassName());

        Directory directory = null;
        DirectoryReader iReader = null;
        IndexWriter indexWriter = null;

        try {
            directory = FSDirectory.open(searchConfig.getPath());

            iReader = DirectoryReader.open(directory);

            IndexWriterConfig config = new IndexWriterConfig(searchConfig.getAnalyzer());

            indexWriter = new IndexWriter(directory,config);

            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            List<SortField> sortFields = new ArrayList<>();

            CSearchPipeContext context = new CSearchPipeContext(OperationTypeEnum.DELETE);

            context.setAnalyzer(searchConfig.getAnalyzer());

            List<JsonFieldContext> fieldContexts = jsc.getFieldContexts();

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

                for(Handle handle : C_FIELD_HANDLES){
                    if(!handle.support(fieldContext)){
                        continue;
                    }
                    Boolean res = (Boolean)handle.handle(fieldContext);
                    sortFields.addAll(fieldContext.getSortFields());

                }

                context.addFieldContext(fieldContext);

            }

            Query query = builder.build();

            if(StringUtils.isEmpty(query.toString())){
                query = new MatchAllDocsQuery();
            }

            indexWriter.deleteDocuments(query);
            indexWriter.forceMergeDeletes();

            System.out.println("删除了 " + indexWriter.hasDeletions());
            System.out.println("还剩 " + indexWriter.numRamDocs());

            return indexWriter.hasDeletions();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        } finally {
            if(indexWriter != null){
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

}
