package com.carot.sec.entity;

import com.carot.sec.annotation.CFieldAdd;
import com.carot.sec.annotation.CFieldQuery;
import com.carot.sec.enums.CFieldTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    @CFieldQuery(enums = CFieldTypeEnum.LONG_POINT,sort = true)
    @CFieldAdd(enums = CFieldTypeEnum.LONG_POINT)
    private Long id ;

    @CFieldQuery(enums = CFieldTypeEnum.TEXT_FIELD)
    @CFieldAdd(enums = CFieldTypeEnum.TEXT_FIELD)
    private String name;

    @CFieldQuery(enums = CFieldTypeEnum.TEXT_FIELD)
    @CFieldAdd(enums = CFieldTypeEnum.TEXT_FIELD,analyzer = true)
    private String desc;

    @CFieldQuery(enums = CFieldTypeEnum.INT_POINT)
    @CFieldAdd(enums = CFieldTypeEnum.INT_POINT)
    private Integer age;

    @CFieldQuery(enums = CFieldTypeEnum.STRING_FIELD)
    @CFieldAdd(enums = CFieldTypeEnum.STRING_FIELD)
    private String url;

    @CFieldQuery(enums = CFieldTypeEnum.LONG_POINT,isDate = true)
    @CFieldAdd(enums = CFieldTypeEnum.LONG_POINT,isDate = true)
    private Date birthDay;

}
