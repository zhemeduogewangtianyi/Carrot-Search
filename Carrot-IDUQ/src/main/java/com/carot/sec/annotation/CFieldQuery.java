package com.carot.sec.annotation;

import com.carot.sec.enums.CFieldTypeEnum;
import org.apache.lucene.search.BooleanClause;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CFieldQuery {

    CFieldTypeEnum enums();

    boolean sort() default false;

    boolean isDate() default false;

    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    BooleanClause.Occur occur() default BooleanClause.Occur.MUST;

}
