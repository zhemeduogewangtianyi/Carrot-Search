package com.carot.sec.operations.update;

import com.carot.sec.annotation.CFieldAdd;
import com.carot.sec.annotation.CFieldQuery;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.entity.User;
import com.carot.sec.handle.HandleInstance;
import com.carot.sec.interfaces.Handle;
import com.carot.sec.operations.query.QueryDoc;
import com.carrot.sec.config.CSearchConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateDoc {

    private static final List<Handle> C_FIELD_HANDLES = HandleInstance.getcFieldHandles();

    public static void main(String[] args) {

        User sourceObject = new User();
        sourceObject.setId(Long.parseLong("2"));
        sourceObject.setName("童童");
        sourceObject.setAge(2);
        sourceObject.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
        sourceObject.setUrl("http://www.baidu.com");
//        sourceObject.setBirthDay(new Date(1624798744527L));

        User targetObject = new User();
        targetObject.setId(Long.parseLong("2"));
        targetObject.setName("沙雕");
        targetObject.setAge(2);
        targetObject.setDesc("沙雕 是一个好学生，太好了，真的是太好了！￥%……");
        targetObject.setUrl("http://www.shadiao.com");
        targetObject.setBirthDay(new Date());

        new UpdateDoc().updateDoc(sourceObject,targetObject);

    }

    private boolean updateDoc(Object sourceObject,Object targetObject) {

        CSearchConfig searchConfig = CSearchConfig.getConfig(sourceObject);

        Directory directory = null;
        DirectoryReader iReader = null;
        IndexWriter indexWriter = null;

        try {
            directory = FSDirectory.open(searchConfig.getPath());

            iReader = DirectoryReader.open(directory);

            IndexWriterConfig config = new IndexWriterConfig(searchConfig.getAnalyzer());

            indexWriter = new IndexWriter(directory,config);

            Class<?> aClass = sourceObject.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();

            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            List<SortField> sortFields = new ArrayList<>();

            for(Field f : declaredFields){
                f.setAccessible(true);
                CSearchPipeContext context = new CSearchPipeContext(builder);
                context.setFieldName(f.getName());
                context.setFieldValue(f.get(sourceObject));
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


            Document doc = new Document();

            Class<?> taClass = targetObject.getClass();
            Field[] tdeclaredFields = taClass.getDeclaredFields();
            for (Field objField : tdeclaredFields) {

                CFieldAdd annotation = objField.getAnnotation(CFieldAdd.class);
                if (annotation != null) {

                    Class<?> type = objField.getType();
                    String name = objField.getName();
                    objField.setAccessible(true);
                    Object o = objField.get(targetObject);

                    C_FIELD_HANDLES.forEach(extendsion -> {

                        CSearchPipeContext context = new CSearchPipeContext(doc);
                        context.setField(objField);
                        context.setCFieldAdd(annotation);
                        context.setFieldValue(o);
                        context.setFieldName(name);
                        context.setClassType(type);

                        if (extendsion.support(context)) {
                            Object handle = extendsion.handle(context);
                            if (handle != null) {
                                IndexableField res = (IndexableField) handle;
                                doc.add(res);

                            }
                        }

                    });
                }
            }

            long l1 = indexWriter.addDocument(doc);

            System.out.println(l1);

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
