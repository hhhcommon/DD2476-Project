16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/unix/struct/DlInfo.java
package com.github.unidbg.unix.struct;

import com.github.unidbg.pointer.UnicornStructure;
import com.sun.jna.Pointer;

import java.util.Arrays;
import java.util.List;

public class DlInfo extends UnicornStructure {

    public DlInfo(Pointer p) {
        super(p);
    }

    public Pointer dli_fname; /* Pathname of shared object */
    public Pointer dli_fbase; /* Base address of shared object */
    public Pointer dli_sname; /* Name of nearest symbol */
    public Pointer dli_saddr; /* Address of nearest symbol */

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("dli_fname", "dli_fbase", "dli_sname", "dli_saddr");
    }

}
