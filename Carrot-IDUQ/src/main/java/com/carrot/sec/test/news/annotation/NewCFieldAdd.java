package com.carrot.sec.test.news.annotation;


import com.carrot.sec.test.news.enums.NewCFieldTypeEnum;

import java.lang.annotation.*;

/**
 * @author wty
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NewCFieldAdd {

    NewCFieldTypeEnum enums();

    boolean store() default true;

    boolean analyzer() default false;

    boolean isDate() default false;

    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

}
