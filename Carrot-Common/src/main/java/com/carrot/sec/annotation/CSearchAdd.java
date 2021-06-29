package com.carrot.sec.annotation;


import com.carrot.sec.enums.NewCFieldTypeEnum;

import java.lang.annotation.*;

/**
 * @author wty
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CSearchAdd {

    NewCFieldTypeEnum enums();

    boolean store() default true;

    boolean analyzer() default false;

}
