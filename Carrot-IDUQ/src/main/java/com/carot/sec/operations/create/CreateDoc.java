package com.carot.sec.operations.create;

import com.carot.sec.annotation.CFieldAdd;
import com.carot.sec.context.CSearchPipeContext;
import com.carot.sec.entity.User;
import com.carot.sec.handle.HandleInstance;
import com.carot.sec.interfaces.Handle;
import com.carrot.sec.config.CSearchConfig;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class CreateDoc {

    private static final List<Handle> C_FIELD_HANDLES = HandleInstance.getcFieldHandles();

    public static void main(String[] args) {

        for(int i = 0 ; i < 100 ; i++){
            User user = new User();
            user.setId(Long.parseLong(i + ""));
            user.setName("周童童");
            user.setAge(i);
            user.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
            user.setUrl("http://www.baidu.com");
            user.setBirthDay(new Date());
            new CreateDoc().createDoc(user);
        }

    }

    public boolean createDoc(Object obj) {

        CSearchConfig searchConfig = CSearchConfig.getConfig(obj);

        Directory directory = null;
        IndexWriter iWriter = null;

        try {
            directory = FSDirectory.open(searchConfig.getPath());

            IndexWriterConfig config = new IndexWriterConfig(searchConfig.getAnalyzer());

            iWriter = new IndexWriter(directory, config);

            Document doc = new Document();

            Class<?> aClass = obj.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field objField : declaredFields) {

                CFieldAdd annotation = objField.getAnnotation(CFieldAdd.class);
                if (annotation != null) {

                    Class<?> type = objField.getType();
                    String name = objField.getName();
                    objField.setAccessible(true);
                    Object o = objField.get(obj);

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

            iWriter.addDocument(doc);

            iWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iWriter != null) {
                try {
                    iWriter.close();
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
