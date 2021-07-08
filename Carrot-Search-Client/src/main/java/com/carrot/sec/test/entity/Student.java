package com.carrot.sec.test.entity;

import com.carrot.sec.annotation.CSearchAdd;
import com.carrot.sec.annotation.CSearchQuery;
import com.carrot.sec.enums.NewCFieldTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class Student {

    @CSearchQuery(enums = NewCFieldTypeEnum.LONG_POINT,sort = true,updateFlag = true)
    @CSearchAdd(enums = NewCFieldTypeEnum.LONG_POINT)
    private Long id ;

    @CSearchQuery(enums = NewCFieldTypeEnum.TEXT_FIELD)
    @CSearchAdd(enums = NewCFieldTypeEnum.TEXT_FIELD)
    private String name;

    @CSearchQuery(enums = NewCFieldTypeEnum.TEXT_FIELD)
    @CSearchAdd(enums = NewCFieldTypeEnum.TEXT_FIELD,analyzer = true)
    private String desc;

    @CSearchQuery(enums = NewCFieldTypeEnum.INT_POINT)
    @CSearchAdd(enums = NewCFieldTypeEnum.INT_POINT)
    private Integer age;

    @CSearchQuery(enums = NewCFieldTypeEnum.STRING_FIELD)
    @CSearchAdd(enums = NewCFieldTypeEnum.STRING_FIELD)
    private String url;

    @CSearchQuery(enums = NewCFieldTypeEnum.LONG_POINT)
    @CSearchAdd(enums = NewCFieldTypeEnum.LONG_POINT)
    private Date birthDay;

}
