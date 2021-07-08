package com.carrot.sec.test;

import com.alibaba.fastjson.JSON;
import com.carrot.sec.annotation.CSearchAdd;
import com.carrot.sec.annotation.CSearchQuery;
import com.carrot.sec.client.CarrotSearchClient;
import com.carrot.sec.config.CSearchConfig;
import com.carrot.sec.context.JsonSearchContext;
import com.carrot.sec.context.add.JsonAddFieldContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.context.query.JsonQueryFieldContext;
import com.carrot.sec.enums.NewCFieldTypeEnum;
import com.carrot.sec.enums.NewOperationTypeEnum;
import com.carrot.sec.enums.OccurEnum;
import com.carrot.sec.test.entity.Student;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

public class TestJsonContext {

    public static void main(String[] args) throws IllegalAccessException {

//        for(int i = 0 ; i < 100000 ; i ++){
//            Student stu = new Student();
//            stu.setId(Long.parseLong(i + ""));
//            stu.setName("周童童");
//            stu.setAge(i);
//            stu.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
//            stu.setUrl("http://www.baidu.com");
////            stu.setBirthDay(new Date());
//            new TestJsonContext().toContextJson(stu,null,null);
//        }

//        Student stu = new Student();
//        stu.setId(Long.parseLong(100 + ""));
//        stu.setName("周童童");
//        stu.setAge(10);
//        stu.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
//        stu.setUrl("http://www.baidu.com");
////            stu.setBirthDay(new Date());
//        new TestJsonContext().toContextJson(stu);

        Student stu = new Student();
//        stu.setId(Long.parseLong(i + ""));
        stu.setId(3211L);
//        stu.setName("周童童");
//        stu.setAge();
//        stu.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
//        stu.setUrl("http://www.baidu.com");
//            stu.setBirthDay(new Date());
        new TestJsonContext().toContextJson(stu,0,10);

    }

    private void toContextJson(Object obj,Integer current,Integer pageSize) throws IllegalAccessException {

        Class<?> aClass = obj.getClass();

        JsonSearchContext context = new JsonSearchContext();
        context.setCurrent(current == null ? 1 : current);
        context.setPageSize(pageSize == null ? 10 : pageSize);
        context.setOperationType(NewOperationTypeEnum.DELETE.getValue());

        //doc name
        String simpleName = aClass.getSimpleName();

        context.setClassName(simpleName);

        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            declaredField.setAccessible(true);

            JsonFieldContext fieldContext = new JsonFieldContext();

            String fieldName = declaredField.getName();
            fieldContext.setFieldName(fieldName);

            Object fieldValue = declaredField.get(obj);
            fieldContext.setFieldValue(fieldValue);

            CSearchAdd cSearchAdd = declaredField.getAnnotation(CSearchAdd.class);

            JsonAddFieldContext addFieldContext = new JsonAddFieldContext();

            //is need analyzer
            boolean analyzer = cSearchAdd.analyzer();
            addFieldContext.setAnalyzer(analyzer);

            //lucene process type
            NewCFieldTypeEnum addTypeEnum = cSearchAdd.enums();
            String addType = addTypeEnum.getType();
            addFieldContext.setType(addType);

            //need store
            boolean store = cSearchAdd.store();
            addFieldContext.setStore(store);

            //add context
            fieldContext.setJsonAddFieldContext(addFieldContext);

            CSearchQuery cSearchQuery = declaredField.getAnnotation(CSearchQuery.class);

            JsonQueryFieldContext queryFieldContext = new JsonQueryFieldContext();

            //condition
            OccurEnum.Occur occur = cSearchQuery.occur();

            String occurStr = occur.toString();
            queryFieldContext.setOccur(occurStr);

            NewCFieldTypeEnum queryTypeEnums = cSearchQuery.enums();
            String queryType = queryTypeEnums.getType();
            queryFieldContext.setType(queryType);

            boolean sort = cSearchQuery.sort();
            queryFieldContext.setSort(sort);

            fieldContext.setJsonQueryFieldContext(queryFieldContext);

            context.addFieldContexts(fieldContext);

        }

        CarrotSearchClient carrotSearchClient = null;
        try {
            carrotSearchClient = new CarrotSearchClient("127.0.0.1", 9527);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String send = carrotSearchClient.send(JSON.toJSONString(context));

        System.out.println(send);

//        try {
//            Connection connection = DriverManager.getConnection("jdbc:carrot-search://127.0.0.1:9527/temp", "root", "root");
//            Statement statement = connection.createStatement();
//            statement.execute(JSON.toJSONString(context));
//            ResultSet resultSet = statement.executeQuery(JSON.toJSONString(context));
//            while (resultSet.next()) {
//                Student unwrap = resultSet.unwrap(Student.class);
//                System.out.println(unwrap);
//            }
//            resultSet.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    static {
        try {
            Class.forName("com.carrot.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
