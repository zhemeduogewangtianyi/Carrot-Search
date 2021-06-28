package com.carrot.sec.test.news;

import com.alibaba.fastjson.JSON;
import com.carrot.sec.entity.User;
import com.carrot.sec.test.news.annotation.NewCFieldAdd;
import com.carrot.sec.test.news.annotation.NewCFieldQuery;
import com.carrot.sec.test.news.context.JsonAddFieldContext;
import com.carrot.sec.test.news.context.JsonContext;
import com.carrot.sec.test.news.context.JsonFieldContext;
import com.carrot.sec.test.news.context.JsonQueryFieldContext;
import com.carrot.sec.test.news.entity.Student;
import com.carrot.sec.test.news.enums.NewCFieldTypeEnum;
import com.carrot.sec.test.news.enums.NewOperationTypeEnum;
import com.carrot.sec.test.news.enums.OccurEnum;

import java.lang.reflect.Field;
import java.util.*;

public class TestJsonContext {

    public static void main(String[] args) throws IllegalAccessException {

        for(int i = 0 ; i < 1 ; i ++){
            Student stu = new Student();
            stu.setId(Long.parseLong(i + ""));
            stu.setName("周童童");
            stu.setAge(i);
            stu.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
            stu.setUrl("http://www.baidu.com");
            stu.setBirthDay(new Date());
            new TestJsonContext().toContextJson(stu);
        }

    }

    private void toContextJson(Object obj) throws IllegalAccessException {

        Class<?> aClass = obj.getClass();

        JsonContext context = new JsonContext(NewOperationTypeEnum.ADD);

        //doc name
        String simpleName = aClass.getSimpleName();

        context.setClassName(simpleName);

        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields){

            declaredField.setAccessible(true);

            JsonFieldContext fieldContext = new JsonFieldContext();

            String fieldName = declaredField.getName();
            fieldContext.setFieldName(fieldName);

            Object fieldValue = declaredField.get(obj);
            fieldContext.setFieldValue(fieldValue);

            NewCFieldAdd cFieldAdd = declaredField.getAnnotation(NewCFieldAdd.class);

            JsonAddFieldContext addFieldContext = new JsonAddFieldContext();

            //is need analyzer
            boolean analyzer = cFieldAdd.analyzer();
            addFieldContext.setAnalyzer(analyzer);

            //lucene process type
            NewCFieldTypeEnum addTypeEnum = cFieldAdd.enums();
            String addType = addTypeEnum.getType();
            addFieldContext.setType(addType);

            //need store
            boolean store = cFieldAdd.store();
            addFieldContext.setStore(store);

            //is Date ?
            boolean addIsDate = cFieldAdd.isDate();
            addFieldContext.setDate(addIsDate);

            //date format
            String addDateFormat = cFieldAdd.dateFormat();
            addFieldContext.setDateFormat(addDateFormat);

            //add context
            fieldContext.setJsonAddFieldContext(addFieldContext);

            NewCFieldQuery cFieldQuery = declaredField.getAnnotation(NewCFieldQuery.class);

            JsonQueryFieldContext queryFieldContext = new JsonQueryFieldContext();

            //condition
            OccurEnum.Occur occur = cFieldQuery.occur();

            String occurStr = occur.toString();
            queryFieldContext.setOccur(occurStr);

            NewCFieldTypeEnum queryTypeEnums = cFieldQuery.enums();
            String queryType = queryTypeEnums.getType();
            queryFieldContext.setType(queryType);

            boolean sort = cFieldQuery.sort();
            queryFieldContext.setSort(sort);

            boolean queryIsDate = cFieldQuery.isDate();
            queryFieldContext.setDate(queryIsDate);

            String queryDateFormat = cFieldQuery.dateFormat();
            queryFieldContext.setDateFormat(queryDateFormat);

            fieldContext.setJsonQueryFIeldContext(queryFieldContext);

            context.addFieldContexts(fieldContext);

        }

        System.out.println(JSON.toJSONString(context));

    }

}
