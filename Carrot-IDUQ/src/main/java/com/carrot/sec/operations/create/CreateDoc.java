package com.carrot.sec.operations.create;

import com.carrot.sec.config.CSearchConfig;
import com.carrot.sec.context.CSearchPipeContext;
import com.carrot.sec.context.JsonSearchContext;
import com.carrot.sec.context.add.CSearchPipeAddContext;
import com.carrot.sec.context.add.JsonAddFieldContext;
import com.carrot.sec.context.field.CSearchPipeFieldContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.enums.CFieldPipeTypeEnum;
import com.carrot.sec.enums.OperationTypeEnum;
import com.carrot.sec.handle.HandleInstance;
import com.carrot.sec.interfaces.Handle;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.List;

public class CreateDoc {

    private static final List<Handle> C_FIELD_HANDLES = HandleInstance.getcFieldHandles();

    public boolean createDoc(JsonSearchContext jsc) throws Exception{

        CSearchConfig searchConfig = CSearchConfig.getConfig(jsc.getClassName());

        Directory directory = null;
        IndexWriter iWriter = null;

        if(jsc.getOperationType() == null || OperationTypeEnum.getByValue(jsc.getOperationType()) == null){
            return false;
        }

        try {
            directory = FSDirectory.open(searchConfig.getPath());

            IndexWriterConfig config = new IndexWriterConfig(searchConfig.getAnalyzer());

            iWriter = new IndexWriter(directory, config);

            Document doc = new Document();

            CSearchPipeContext context = new CSearchPipeContext(OperationTypeEnum.ADD);

            context.setAnalyzer(config.getAnalyzer());

            List<JsonFieldContext> fieldContexts = jsc.getFieldContexts();

            for(JsonFieldContext jfc : fieldContexts){


                CSearchPipeFieldContext fieldContext = new CSearchPipeFieldContext();

                String fieldName = jfc.getFieldName();
                Object fieldValue = jfc.getFieldValue();

                fieldContext.setFieldName(fieldName);
                fieldContext.setFieldValue(fieldValue);

                JsonAddFieldContext addFieldContext = jfc.getJsonAddFieldContext();
                String addType = addFieldContext.getType();
                boolean addAnalyzer = addFieldContext.isAnalyzer();
                boolean addStore = addFieldContext.isStore();

                CSearchPipeAddContext addContext = new CSearchPipeAddContext(doc);

                addContext.setEnums(CFieldPipeTypeEnum.getEnumByFlag(addType));
                addContext.setAnalyzer(addAnalyzer);
                addContext.setStore(addStore);

                fieldContext.setAddContext(addContext);

                context.addFieldContext(fieldContext);

                for(Handle handle : C_FIELD_HANDLES){
                    if (handle.support(fieldContext)) {
                        Object res = handle.handle(fieldContext);
                        if (res != null) {
                            IndexableField idx = (IndexableField) res;
                            doc.add(idx);
                        }
                    }
                }


            }

            iWriter.addDocument(doc);

            iWriter.close();

            return true;

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        } finally {
            if (iWriter != null) {
                try {
                    iWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
