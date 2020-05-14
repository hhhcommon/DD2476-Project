16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/ios/struct/objc/ObjcClass.java
package com.github.unidbg.ios.struct.objc;

import com.github.unidbg.Emulator;
import com.github.unidbg.pointer.UnicornPointer;
import com.sun.jna.Pointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * objc_class
 */
public class ObjcClass extends ObjcObject implements ObjcConstants {

    public static ObjcClass create(Emulator<?> emulator, Pointer pointer) {
        ObjcClass objcClass = new ObjcClass(emulator, pointer);
        objcClass.unpack();
        return objcClass;
    }

    private ObjcClass(Emulator<?> emulator, Pointer p) {
        super(emulator, p);
    }

    public Pointer superClass;
    public Pointer cache;
    public Pointer vtable;
    public Pointer data;

    @Override
    protected List<String> getFieldOrder() {
        List<String> fields = new ArrayList<>(super.getFieldOrder());
        Collections.addAll(fields, "superClass", "cache", "vtable", "data");
        return fields;
    }

    private ClassRW data() {
        UnicornPointer pointer = (UnicornPointer) data;
        long address = pointer.peer & ~CLASS_FAST_FLAG_MASK;
        ClassRW classRW = new ClassRW(UnicornPointer.pointer(emulator, address));
        classRW.unpack();
        return classRW;
    }

    private ClassRO ro() {
        UnicornPointer pointer = (UnicornPointer) data;
        long address = pointer.peer & ~CLASS_FAST_FLAG_MASK;
        ClassRO classRO = new ClassRO(UnicornPointer.pointer(emulator, address));
        classRO.unpack();
        return classRO;
    }

    public boolean isMetaClass() {
        return (data().ro().flags & RO_META) != 0;
    }

    public ObjcClass getMeta() {
        if (isMetaClass()) {
            return this;
        } else {
            return getObjClass();
        }
    }

    private boolean isRealized() {
        return (data().flags & RW_REALIZED) != 0;
    }

    private boolean isFuture() {
        return (data().flags & RO_FUTURE) != 0;
    }

    public String getName() {
        if (isRealized()  ||  isFuture()) {
            return data().ro().name.getString(0);
        } else {
            return ro().name.getString(0);
        }
    }

}
