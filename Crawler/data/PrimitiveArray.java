16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/linux/android/dvm/array/PrimitiveArray.java
package com.github.unidbg.linux.android.dvm.array;

import com.github.unidbg.Emulator;
import com.github.unidbg.linux.android.dvm.Array;
import com.github.unidbg.pointer.UnicornPointer;
import com.sun.jna.Pointer;

public interface PrimitiveArray<T> extends Array<T> {

    UnicornPointer _GetArrayCritical(Emulator<?> emulator, Pointer isCopy);

    void _ReleaseArrayCritical(Pointer elems, int mode);

}
