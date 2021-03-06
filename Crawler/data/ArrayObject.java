16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/linux/android/dvm/array/ArrayObject.java
package com.github.unidbg.linux.android.dvm.array;

import com.github.unidbg.linux.android.dvm.Array;
import com.github.unidbg.linux.android.dvm.DvmObject;

public class ArrayObject extends BaseArray<DvmObject<?>[]> implements Array<DvmObject<?>[]> {

    public ArrayObject(DvmObject<?>...value) {
        super(value);
    }

    @Override
    public int length() {
        return value.length;
    }

    @Override
    public void setData(int start, DvmObject<?>[] data) {
        System.arraycopy(data, 0, value, start, data.length);
    }
}
