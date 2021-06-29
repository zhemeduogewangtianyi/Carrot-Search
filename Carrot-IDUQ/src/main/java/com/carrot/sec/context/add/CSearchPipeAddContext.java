package com.carrot.sec.context.add;

import com.carrot.sec.enums.CFieldPipeTypeEnum;
import lombok.Data;
import org.apache.lucene.document.Document;

@Data
public class CSearchPipeAddContext {

    private final Document doc;

    private CFieldPipeTypeEnum enums;

    private boolean store;

    private boolean analyzer;

    public CSearchPipeAddContext(Document doc) {
        this.doc = doc;
    }

}
