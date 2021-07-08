package com.carrot.sec.test;

import com.alibaba.fastjson.JSON;
import com.carrot.sec.annotation.CSearchAdd;
import com.carrot.sec.annotation.CSearchQuery;
import com.carrot.sec.client.CarrotSearchClient;
import com.carrot.sec.config.CSearchConfig;
import com.carrot.sec.context.JsonSearchContext;
import com.carrot.sec.context.OperationHandleContext;
import com.carrot.sec.context.add.JsonAddFieldContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.context.query.JsonQueryFieldContext;
import com.carrot.sec.enums.NewCFieldTypeEnum;
import com.carrot.sec.enums.NewOperationTypeEnum;
import com.carrot.sec.enums.OccurEnum;
import com.carrot.sec.interfaces.Handle;
import com.carrot.sec.test.entity.Student;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestJsonContext {

    private static final List<Handle> OPERATION_HANDLES = new ArrayList<>();

    static {
        ExtensionLoader<Handle> extensionLoader = ExtensionLoader.getExtensionLoader(Handle.class);
        for (NewOperationTypeEnum name : NewOperationTypeEnum.values()) {
            try {
                OPERATION_HANDLES.add(extensionLoader.getExtension(name.getKey()));
            } catch (Exception e) {
                continue;
            }
        }
    }


    public void toContextJson(Object obj, Integer current, Integer pageSize) throws Throwable {

        Class<?> aClass = obj.getClass();

        JsonSearchContext context = new JsonSearchContext();
        context.setCurrent(current == null ? 1 : current);
        context.setPageSize(pageSize == null ? 10000 : pageSize);
        context.setOperationType(NewOperationTypeEnum.QUERY.getValue());

        //doc name
        String simpleName = aClass.getSimpleName();

        context.setClassName(simpleName);

        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            declaredField.setAccessible(true);

            NewOperationTypeEnum typeEnum = NewOperationTypeEnum.getByValue(context.getOperationType());
            OperationHandleContext handleContext = new OperationHandleContext(declaredField, typeEnum, obj);

            for (Handle handle : OPERATION_HANDLES) {
                if (!handle.support(handleContext)) {
                    continue;
                }
                JsonFieldContext res = (JsonFieldContext) handle.handle(handleContext);
                context.addFieldContexts(res);

            }

        }

        if(NewOperationTypeEnum.getByValue(context.getOperationType()).equals(NewOperationTypeEnum.UPDATE)){
            boolean checkUpdateFlag = false;
            List<JsonFieldContext> fieldContexts = context.getFieldContexts();
            for(JsonFieldContext fieldContext : fieldContexts){
                JsonQueryFieldContext jsonQueryFieldContext = fieldContext.getJsonQueryFieldContext();
                if(jsonQueryFieldContext != null){
                    checkUpdateFlag = true;
                }
            }
            if(!checkUpdateFlag){
                throw new RuntimeException("update flag can't null !");
            }
        }

//        System.out.println(JSON.toJSONString(context));

//        CarrotSearchClient carrotSearchClient = null;
//        try {
//            carrotSearchClient = new CarrotSearchClient("127.0.0.1", 9527);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String send = carrotSearchClient.send(JSON.toJSONString(context));
//
//        System.out.println(send);

        try {
            Connection connection = DriverManager.getConnection("jdbc:carrot-search://127.0.0.1:9527/temp", "root", "root");
            Statement statement = connection.createStatement();

//            boolean execute = statement.execute(JSON.toJSONString(context));
//            System.out.println(execute);
//            statement.close();

            ResultSet resultSet = statement.executeQuery(JSON.toJSONString(context));
            while (resultSet.next()) {
                Student unwrap = resultSet.unwrap(Student.class);
//                System.out.println(unwrap);
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static {
        try {
            Class.forName("com.carrot.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
