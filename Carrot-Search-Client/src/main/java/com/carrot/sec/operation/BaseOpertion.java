package com.carrot.sec.operation;

import com.carrot.sec.context.JsonSearchContext;
import com.carrot.sec.context.OperationHandleContext;
import com.carrot.sec.context.field.JsonFieldContext;
import com.carrot.sec.enums.NewOperationTypeEnum;
import com.carrot.sec.interfaces.Handle;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.ArrayList;
import java.util.List;

public class BaseOpertion {

    protected static final List<Handle> OPERATION_HANDLES = new ArrayList<>();

    static {
        try {
            Class.forName("com.carrot.jdbc.Driver");

            ExtensionLoader<Handle> extensionLoader = ExtensionLoader.getExtensionLoader(Handle.class);
            for (NewOperationTypeEnum name : NewOperationTypeEnum.values()) {
                try {
                    OPERATION_HANDLES.add(extensionLoader.getExtension(name.getKey()));
                } catch (Exception e) {
                    continue;
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void build(JsonSearchContext context, OperationHandleContext handleContext) throws Throwable {
        for (Handle handle : OPERATION_HANDLES) {

            if (!handle.support(handleContext)) {
                continue;
            }
            JsonFieldContext res = (JsonFieldContext) handle.handle(handleContext);
            context.addFieldContexts(res);

        }
    }

}
