package com.carot.sec.annotation;


import com.carot.sec.enums.CFieldTypeEnum;
import java.lang.annotation.*;

/**
 * @author wty
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CFieldAdd {

    CFieldTypeEnum enums();

    boolean store() default true;

    boolean analyzer() default false;

    boolean isDate() default false;

    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

}
