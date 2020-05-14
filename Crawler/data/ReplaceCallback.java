16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/hook/ReplaceCallback.java
package com.github.unidbg.hook;

import com.github.unidbg.Emulator;
import com.github.unidbg.arm.HookStatus;

public abstract class ReplaceCallback {

    public  HookStatus onCall(Emulator<?> emulator, long originFunction) {
        return HookStatus.RET(emulator, originFunction);
    }

    public  HookStatus onCall(Emulator<?> emulator, HookContext context, long originFunction) {
        return onCall(emulator, originFunction);
    }

    public void postCall(Emulator<?> emulator, HookContext context) {
    }

}
