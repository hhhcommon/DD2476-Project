16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/net/fornwall/jelf/AndroidRelocation.java
package net.fornwall.jelf;

import java.util.Iterator;

public class AndroidRelocation implements Iterable<MemoizedObject<ElfRelocation>> {

    private final ElfParser parser;
    private final SymbolLocator symtab;
    private final byte[] androidRelData;
    private final boolean rela;

    AndroidRelocation(ElfParser parser, SymbolLocator symtab, byte[] androidRelData, boolean rela) {
        this.parser = parser;
        this.symtab = symtab;
        this.androidRelData = androidRelData;
        this.rela = rela;
    }

    @Override
    public Iterator<MemoizedObject<ElfRelocation>> iterator() {
        return new AndroidRelocationIterator(parser.elfFile.objectSize, symtab, androidRelData, rela);
    }
}
