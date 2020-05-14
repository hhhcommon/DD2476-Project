16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/ios/struct/kernel/VprocMigLookupData.java
package com.github.unidbg.ios.struct.kernel;

import com.github.unidbg.pointer.UnicornStructure;
import com.sun.jna.Pointer;

import java.util.Arrays;
import java.util.List;

public class VprocMigLookupData extends UnicornStructure {

    public VprocMigLookupData(Pointer p) {
        super(p);
    }

    public int ret;
    public int size;
    public AuditToken au_tok;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("ret", "size", "au_tok");
    }

}
