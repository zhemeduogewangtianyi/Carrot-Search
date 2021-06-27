package com.carot.sec.operations.query;

import com.carot.sec.annotation.CFieldQuery;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.entity.User;
import com.carot.sec.handle.HandleInstance;
import com.carot.sec.interfaces.Handle;
import com.carrot.sec.config.CSearchConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryDoc {

    private static final List<Handle> C_FIELD_HANDLES = HandleInstance.getcFieldHandles();

    public static void main(String[] args) {

        User user = new User();
        user.setId(Long.parseLong("2"));
        user.setName("童童");
        user.setAge(2);
        user.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
        user.setUrl("http://www.baidu.com");
//        user.setBirthDay(new Date());
        new QueryDoc().queryDoc(user,1,10);

    }

    private boolean queryDoc(Object obj,int current,int pageSize) {

        CSearchConfig searchConfig = CSearchConfig.getConfig(obj);

        Directory directory = null;
        DirectoryReader iReader = null;

        try {
            directory = FSDirectory.open(searchConfig.getPath());

            iReader = DirectoryReader.open(directory);

            IndexSearcher indexSearcher = new IndexSearcher(iReader);

            Class<?> aClass = obj.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();

            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            List<SortField> sortFields = new ArrayList<>();

            for(Field f : declaredFields){
                f.setAccessible(true);
                CSearchPipeContext context = new CSearchPipeContext(builder);
                context.setFieldName(f.getName());
                context.setFieldValue(f.get(obj));
                context.setCFieldQuery(f.getAnnotation(CFieldQuery.class));
                context.setField(f);
                context.setClassType(aClass);
                context.setAnalyzer(searchConfig.getAnalyzer());

                for(Handle handle : C_FIELD_HANDLES){
                    if(!handle.support(context)){
                        continue;
                    }
                    Boolean res = (Boolean)handle.handle(context);
                    if(res){
                        sortFields.addAll(context.getSortFields());
                    }
                }

            }

            Query query = builder.build();

            if(StringUtils.isEmpty(query.toString())){
                query = new MatchAllDocsQuery();
            }
            

            ScoreDoc before = null;
            if(current != 1){

                Sort sort = new Sort(sortFields.toArray(new SortField[]{}));

                TopDocs docsBefore = indexSearcher.search(query, (current-1)*pageSize , sort , true);
                ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
                if(scoreDocs.length > 0){
                    before = scoreDocs[scoreDocs.length - 1];
                }
            }

            Sort sort = new Sort(sortFields.toArray(new SortField[]{}));
            TopDocs docs = indexSearcher.searchAfter(before, query, pageSize , sort , false);

            ScoreDoc[] scoreDocs = docs.scoreDocs;
            System.out.println("所有的数据总数为："+docs.totalHits);
            System.out.println("本页查询到的总数为："+scoreDocs.length);
            for(ScoreDoc hit : scoreDocs){
                Document hitDoc = indexSearcher.doc(hit.doc);
                List<IndexableField> fields = hitDoc.getFields();
                for(IndexableField idxField : fields){
                    System.out.println(idxField.name() + " : " + idxField.getCharSequenceValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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


        return true;

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
