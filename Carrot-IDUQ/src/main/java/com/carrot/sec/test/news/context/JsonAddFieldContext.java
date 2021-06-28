package com.carrot.sec.test.news.context;

import lombok.Data;

@Data
public class JsonAddFieldContext {

    private boolean analyzer;

    String type;

    private boolean store;

    private boolean isDate;

    private String dateFormat;

}
