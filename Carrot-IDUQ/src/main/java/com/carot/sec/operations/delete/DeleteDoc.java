package com.carot.sec.operations.delete;

import com.carot.sec.annotation.CFieldQuery;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.entity.User;
import com.carot.sec.handle.HandleInstance;
import com.carot.sec.interfaces.Handle;
import com.carrot.sec.config.CSearchConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DeleteDoc {

    private static final List<Handle> C_FIELD_HANDLES = HandleInstance.getcFieldHandles();

    public static void main(String[] args) {

        User obj = new User();
        obj.setId(Long.parseLong("2"));
        obj.setName("沙雕");
//        obj.setAge(2);
//        obj.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
//        obj.setUrl("http://www.baidu.com");
//        obj.setBirthDay(new Date());

        new DeleteDoc().deleteDoc(obj);

    }

    private boolean deleteDoc(Object obj) {

        CSearchConfig searchConfig = CSearchConfig.getConfig(obj);

        Directory directory = null;
        DirectoryReader iReader = null;
        IndexWriter indexWriter = null;

        try {
            directory = FSDirectory.open(searchConfig.getPath());

            iReader = DirectoryReader.open(directory);

            IndexWriterConfig config = new IndexWriterConfig(searchConfig.getAnalyzer());

            indexWriter = new IndexWriter(directory,config);

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
                context.setClassType(f.getType());
                context.setAnalyzer(searchConfig.getAnalyzer());

                for(Handle handle : C_FIELD_HANDLES){
                    if(!handle.support(context)){
                        continue;
                    }
                    Boolean res = (Boolean)handle.handle(context);
                    sortFields.addAll(context.getSortFields());

                }

            }

            Query query = builder.build();

            if(StringUtils.isEmpty(query.toString())){
                query = new MatchAllDocsQuery();
            }

            long l = indexWriter.deleteDocuments(query);
            System.out.println(l);


        } catch (Exception e) {
            e.printStackTrace();
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

        return true;

    }

}
