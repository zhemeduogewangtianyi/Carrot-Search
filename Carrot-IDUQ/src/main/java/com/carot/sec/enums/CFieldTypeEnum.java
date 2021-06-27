package com.carot.sec.enums;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.SortField;

public enum CFieldTypeEnum {
    /***/
    INT_POINT("存储 int 类型" , IntPoint.class,SortField.Type.INT),
    LONG_POINT("存储 long 类型" ,NumericDocValuesField.class, SortField.Type.LONG),
    TEXT_FIELD("存储 string 类型，分词" ,TextField.class,SortField.Type.STRING_VAL),
    STRING_FIELD("存储 string 类型，不分词" ,StringField.class,SortField.Type.STRING_VAL),
    STORED_FIELD("存储类型" ,StoredField.class,null),


    ;

    private final String desc;
    private final Class<?> cls;
    private final SortField.Type type;

    CFieldTypeEnum(String desc , Class<?> cls,SortField.Type type) {
        this.cls = cls;
        this.desc = desc;
        this.type = type;
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
}
