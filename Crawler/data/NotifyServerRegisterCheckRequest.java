16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/ios/struct/kernel/NotifyServerRegisterCheckRequest.java
package com.github.unidbg.ios.struct.kernel;

import com.github.unidbg.pointer.UnicornStructure;
import com.sun.jna.Pointer;

import java.util.Arrays;
import java.util.List;

public class NotifyServerRegisterCheckRequest extends UnicornStructure {

    public NotifyServerRegisterCheckRequest(Pointer p) {
        super(p);
    }

    public int pad;
    public int name;
    public int namelen;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("pad", "name", "namelen");
    }

}
