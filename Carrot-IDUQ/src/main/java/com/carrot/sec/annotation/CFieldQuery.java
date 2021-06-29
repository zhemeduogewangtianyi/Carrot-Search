package com.carrot.sec.annotation;

import com.carrot.sec.enums.CFieldPipeTypeEnum;
import org.apache.lucene.search.BooleanClause;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CFieldQuery {

    CFieldPipeTypeEnum enums();

    boolean sort() default false;

    boolean isDate() default false;

    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    BooleanClause.Occur occur() default BooleanClause.Occur.MUST;

}
