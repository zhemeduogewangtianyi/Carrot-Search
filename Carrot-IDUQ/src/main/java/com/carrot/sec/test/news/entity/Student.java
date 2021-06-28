package com.carrot.sec.test.news.entity;

import com.carrot.sec.enums.CFieldTypeEnum;
import com.carrot.sec.test.news.annotation.NewCFieldAdd;
import com.carrot.sec.test.news.annotation.NewCFieldQuery;
import com.carrot.sec.test.news.enums.NewCFieldTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class Student {

    @NewCFieldQuery(enums = NewCFieldTypeEnum.LONG_POINT,sort = true)
    @NewCFieldAdd(enums = NewCFieldTypeEnum.LONG_POINT)
    private Long id ;

    @NewCFieldQuery(enums = NewCFieldTypeEnum.TEXT_FIELD)
    @NewCFieldAdd(enums = NewCFieldTypeEnum.TEXT_FIELD)
    private String name;

    @NewCFieldQuery(enums = NewCFieldTypeEnum.TEXT_FIELD)
    @NewCFieldAdd(enums = NewCFieldTypeEnum.TEXT_FIELD,analyzer = true)
    private String desc;

    @NewCFieldQuery(enums = NewCFieldTypeEnum.INT_POINT)
    @NewCFieldAdd(enums = NewCFieldTypeEnum.INT_POINT)
    private Integer age;

    @NewCFieldQuery(enums = NewCFieldTypeEnum.STRING_FIELD)
    @NewCFieldAdd(enums = NewCFieldTypeEnum.STRING_FIELD)
    private String url;

    @NewCFieldQuery(enums = NewCFieldTypeEnum.LONG_POINT,isDate = true)
    @NewCFieldAdd(enums = NewCFieldTypeEnum.LONG_POINT,isDate = true)
    private Date birthDay;

}
