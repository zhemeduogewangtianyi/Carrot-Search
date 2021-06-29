package com.carrot.sec.enums;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.SortField;

public enum CFieldPipeTypeEnum {
    /***/
    INT_POINT("存储 int 类型" , IntPoint.class,SortField.Type.INT,"INT"),
    LONG_POINT("存储 long 类型" ,NumericDocValuesField.class, SortField.Type.LONG,"LONG"),
    TEXT_FIELD("存储 string 类型，分词" ,TextField.class,SortField.Type.STRING_VAL,"TEXT"),
    STRING_FIELD("存储 string 类型，不分词" ,StringField.class,SortField.Type.STRING_VAL,"STRING"),
    STORED_FIELD("存储类型" ,StoredField.class,null,"STORED"),


    ;

    private final String desc;
    private final Class<?> cls;
    private final SortField.Type type;
    private final String flag;

    CFieldPipeTypeEnum(String desc , Class<?> cls, SortField.Type type,String flag) {
        this.cls = cls;
        this.desc = desc;
        this.type = type;
        this.flag = flag;
    }

    public IndexableField getClsField(Class<?>[] parameter, Object[] args) {
        try {
            Object o = cls.getDeclaredConstructor(parameter).newInstance(args);
            return (IndexableField)o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public SortField.Type getType() {
        return type;
    }

    public String getFlag() {
        return flag;
    }

    public static CFieldPipeTypeEnum getEnumByFlag(String flag){
        for(CFieldPipeTypeEnum typeEnum : CFieldPipeTypeEnum.values()){
            if(typeEnum.getFlag().equals(flag)){
                return typeEnum;
            }
        }
        return null;
    }
}
