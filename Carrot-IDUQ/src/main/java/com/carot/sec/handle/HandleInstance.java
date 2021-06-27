package com.carot.sec.handle;

import com.carot.sec.enums.CFieldAddEnum;
import com.carot.sec.enums.CFieldQueryEnum;
import com.carot.sec.interfaces.Handle;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.ArrayList;
import java.util.List;

public class HandleInstance {

    private static final List<Handle> C_FIELD_HANDLES = new ArrayList<>();

    static {
        ExtensionLoader<Handle> extensionLoader = ExtensionLoader.getExtensionLoader(Handle.class);
        for (CFieldAddEnum name : CFieldAddEnum.values()) {
            C_FIELD_HANDLES.add(extensionLoader.getExtension(name.getName()));
        }
        for (CFieldQueryEnum name : CFieldQueryEnum.values()) {
            C_FIELD_HANDLES.add(extensionLoader.getExtension(name.getName()));
        }
    }

    public static List<Handle> getcFieldHandles() {
        return C_FIELD_HANDLES;
    }
}
