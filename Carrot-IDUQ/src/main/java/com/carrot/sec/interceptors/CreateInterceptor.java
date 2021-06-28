package com.carrot.sec.interceptors;

import com.carrot.sec.context.CSearchPipeContext;
import com.carrot.sec.interfaces.Handle;
import com.carrot.sec.interfaces.Interceptor;
import org.apache.lucene.index.IndexableField;

public class CreateInterceptor implements Interceptor<CSearchPipeContext,IndexableField>{

    @Override
    public boolean available(CSearchPipeContext cSearchPipeContext) {
        return true;
    }

    @Override
    public Object pre(Handle<CSearchPipeContext, IndexableField> handle, CSearchPipeContext cSearchPipeContext) throws Throwable {
        return null;
    }

    @Override
    public IndexableField post(Handle<CSearchPipeContext, IndexableField> handle, CSearchPipeContext cSearchPipeContext, Object result) throws Throwable {
        return null;
    }

    @Override
    public IndexableField after(Handle<CSearchPipeContext, IndexableField> handle, CSearchPipeContext cSearchPipeContext, Throwable e) throws Throwable {
        return null;
    }

    @Override
    public IndexableField hook(Handle<CSearchPipeContext, IndexableField> handle, CSearchPipeContext cSearchPipeContext) throws Throwable {
        return null;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public boolean support(CSearchPipeContext cSearchPipeContext) {
//        OperationTypeEnum operationTypeEnum = cSearchPipeContext.getOperationTypeEnum();
//        if(operationTypeEnum == null){
//            return false;
//        }
//        return operationTypeEnum.equals(OperationTypeEnum.ADD);
        return true;
    }
}
