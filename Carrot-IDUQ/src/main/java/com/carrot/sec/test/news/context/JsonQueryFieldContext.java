package com.carrot.sec.test.news.context;

import lombok.Data;

@Data
public class JsonQueryFieldContext {


    private String occur;

    boolean sort;

    boolean isDate;

    String dateFormat;

    String type;

}
