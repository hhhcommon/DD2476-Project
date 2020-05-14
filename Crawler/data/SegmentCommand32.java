16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/ios/struct/SegmentCommand32.java
package com.github.unidbg.ios.struct;

import com.sun.jna.Pointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SegmentCommand32 extends SegmentCommand {

    public SegmentCommand32(Pointer p) {
        super(p);
    }

    public int vmaddr;

    @Override
    public long getVmAddress() {
        return vmaddr;
    }

    @Override
    protected List<String> getFieldOrder() {
        List<String> list = new ArrayList<>(super.getFieldOrder());
        Collections.addAll(list, "segname", "vmaddr");
        return list;
    }
}
