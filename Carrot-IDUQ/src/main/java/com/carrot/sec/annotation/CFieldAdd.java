package com.carrot.sec.annotation;


import com.carrot.sec.enums.CFieldPipeTypeEnum;
import java.lang.annotation.*;

/**
 * @author wty
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CFieldAdd {

    CFieldPipeTypeEnum enums();

    boolean store() default true;

    boolean analyzer() default false;

    boolean isDate() default false;

    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

}
