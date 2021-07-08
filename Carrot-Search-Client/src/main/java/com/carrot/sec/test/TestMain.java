package com.carrot.sec.test;

import com.carrot.sec.test.entity.Student;

public class TestMain {

    public static void main(String[] args) throws Throwable {

//        for(int i = 0 ; i < 10 ; i ++){
//            Student stu = new Student();
//            stu.setId(Long.parseLong(i + ""));
//            stu.setName("周多余 1 + 1");
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

        long start = System.currentTimeMillis();
        Student stu = new Student();
//        stu.setId(Long.parseLong(i + ""));
//        stu.setId(3212L);
//        stu.setName("周童童");
//        stu.setAge(3212);
//        stu.setDesc("周童童 是一个好学生，太好了，真的是太好了！￥%……");
//        stu.setUrl("http://www.baidu.com");
//            stu.setBirthDay(new Date());
        new TestJsonContext().toContextJson(stu,0,1000000000);

        System.out.println(System.currentTimeMillis() - start + " ms");

    }

}
