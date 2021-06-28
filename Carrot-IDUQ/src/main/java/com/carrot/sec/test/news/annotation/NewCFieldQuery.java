package com.carrot.sec.test.news.annotation;

import com.carrot.sec.test.news.enums.NewCFieldTypeEnum;
import com.carrot.sec.test.news.enums.OccurEnum;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NewCFieldQuery {

    NewCFieldTypeEnum enums();

    boolean sort() default false;

    boolean isDate() default false;

    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    OccurEnum.Occur occur() default OccurEnum.Occur.MUST;

}
