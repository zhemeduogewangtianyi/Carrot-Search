package com.carrot.sec.operation;

import com.alibaba.fastjson.JSON;
import com.carrot.sec.context.JsonSearchContext;
import com.carrot.sec.context.OperationHandleContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.context.query.JsonQueryFieldContext;
import com.carrot.sec.enums.NewOperationTypeEnum;
import com.carrot.sec.interfaces.Operations;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Operation extends BaseOpertion implements Operations {

    @Override
    public boolean create(Object obj) throws Throwable {
        Class<?> aClass = obj.getClass();

        JsonSearchContext context = new JsonSearchContext();
        context.setOperationType(NewOperationTypeEnum.ADD.getValue());

        //doc name
        String simpleName = aClass.getSimpleName();

        context.setClassName(simpleName);

        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            declaredField.setAccessible(true);

            NewOperationTypeEnum typeEnum = NewOperationTypeEnum.getByValue(context.getOperationType());
            OperationHandleContext handleContext = new OperationHandleContext(declaredField, typeEnum, obj);

            //构建上下文
            build(context, handleContext);

        }

        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:carrot-search://127.0.0.1:9527/temp", "root", "root");
            statement = connection.createStatement();
            return statement.execute(JSON.toJSONString(context));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public boolean delete(Object obj) throws Throwable {
        Class<?> aClass = obj.getClass();

        JsonSearchContext context = new JsonSearchContext();
        context.setOperationType(NewOperationTypeEnum.DELETE.getValue());

        //doc name
        String simpleName = aClass.getSimpleName();

        context.setClassName(simpleName);

        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            declaredField.setAccessible(true);

            NewOperationTypeEnum typeEnum = NewOperationTypeEnum.getByValue(context.getOperationType());
            OperationHandleContext handleContext = new OperationHandleContext(declaredField, typeEnum, obj);

            //构建上下文
            build(context, handleContext);

        }

        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:carrot-search://127.0.0.1:9527/temp", "root", "root");
            statement = connection.createStatement();
            return statement.execute(JSON.toJSONString(context));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public boolean update(Object obj) throws Throwable {

        Class<?> aClass = obj.getClass();

        JsonSearchContext context = new JsonSearchContext();
        context.setOperationType(NewOperationTypeEnum.UPDATE.getValue());

        //doc name
        String simpleName = aClass.getSimpleName();

        context.setClassName(simpleName);

        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            declaredField.setAccessible(true);

            NewOperationTypeEnum typeEnum = NewOperationTypeEnum.getByValue(context.getOperationType());
            OperationHandleContext handleContext = new OperationHandleContext(declaredField, typeEnum, obj);

            //构建上下文
            build(context, handleContext);

        }

        boolean checkUpdateFlag = false;
        List<JsonFieldContext> fieldContexts = context.getFieldContexts();
        for (JsonFieldContext fieldContext : fieldContexts) {
            JsonQueryFieldContext jsonQueryFieldContext = fieldContext.getJsonQueryFieldContext();
            if (jsonQueryFieldContext != null) {
                checkUpdateFlag = true;
                break;
            }
        }
        if (!checkUpdateFlag) {
            throw new RuntimeException("update flag can't null !");
        }

        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:carrot-search://127.0.0.1:9527/temp", "root", "root");
            statement = connection.createStatement();

            return statement.execute(JSON.toJSONString(context));

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public List<Map<String, Object>> select(Object obj, Integer current, Integer pageSize) throws Throwable {

        List<Map<String, Object>> result = new ArrayList<>();

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

            //构建上下文
            build(context, handleContext);

        }

        if (NewOperationTypeEnum.getByValue(context.getOperationType()).equals(NewOperationTypeEnum.UPDATE)) {
            boolean checkUpdateFlag = false;
            List<JsonFieldContext> fieldContexts = context.getFieldContexts();
            for (JsonFieldContext fieldContext : fieldContexts) {
                JsonQueryFieldContext jsonQueryFieldContext = fieldContext.getJsonQueryFieldContext();
                if (jsonQueryFieldContext != null) {
                    checkUpdateFlag = true;
                }
            }
            if (!checkUpdateFlag) {
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

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:carrot-search://127.0.0.1:9527/temp", "root", "root");
            statement = connection.createStatement();

//            boolean execute = statement.execute(JSON.toJSONString(context));
//            System.out.println(execute);
//            statement.close();

            resultSet = statement.executeQuery(JSON.toJSONString(context));
            while (resultSet.next()) {
                result.add(resultSet.unwrap(Map.class));
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(resultSet != null){
                resultSet.close();
            }
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
        return result;
    }

}
