package com.carrot.sec.annotation;

import com.carrot.sec.enums.NewCFieldTypeEnum;
import com.carrot.sec.enums.OccurEnum;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CSearchQuery {

    NewCFieldTypeEnum enums();

    boolean sort() default false;

    OccurEnum.Occur occur() default OccurEnum.Occur.MUST;

}
