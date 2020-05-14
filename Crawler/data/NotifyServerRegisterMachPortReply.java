16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/ios/struct/kernel/NotifyServerRegisterMachPortReply.java
package com.github.unidbg.ios.struct.kernel;

import com.github.unidbg.pointer.UnicornStructure;
import com.sun.jna.Pointer;

import java.util.Arrays;
import java.util.List;

public class NotifyServerRegisterMachPortReply extends UnicornStructure {

    public NotifyServerRegisterMachPortReply(Pointer p) {
        super(p);
    }

    public MachMsgBody body;
    public int ret;
    public int code;
    public int clientId;
    public int status;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("body", "ret", "code", "clientId", "status");
    }
}