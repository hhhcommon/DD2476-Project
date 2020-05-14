16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/ios/struct/kernel/VmRegionRecurse64Request.java
package com.github.unidbg.ios.struct.kernel;

import com.github.unidbg.pointer.UnicornStructure;
import com.sun.jna.Pointer;

import java.util.Arrays;
import java.util.List;

public class VmRegionRecurse64Request extends UnicornStructure {

    public VmRegionRecurse64Request(Pointer p) {
        super(p);
    }

    public NDR_record NDR;
    public long address;
    public int nestingDepth;
    public int infoCnt;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("NDR", "address", "nestingDepth", "infoCnt");
    }

    public long getAddress() {
        return address;
    }

}
