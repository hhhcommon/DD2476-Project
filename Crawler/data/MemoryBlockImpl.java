16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/memory/MemoryBlockImpl.java
package com.github.unidbg.memory;

import com.github.unidbg.pointer.UnicornPointer;
import com.sun.jna.Pointer;
import unicorn.UnicornConst;

public class MemoryBlockImpl implements MemoryBlock {

    public static MemoryBlock alloc(Memory memory, int length) {
        UnicornPointer pointer = memory.mmap(length, UnicornConst.UC_PROT_READ | UnicornConst.UC_PROT_WRITE);
        return new MemoryBlockImpl(memory, pointer);
    }

    public static MemoryBlock allocExecutable(Memory memory, int length) {
        UnicornPointer pointer = memory.mmap(length, UnicornConst.UC_PROT_READ | UnicornConst.UC_PROT_EXEC);
        return new MemoryBlockImpl(memory, pointer);
    }

    private final Memory memory;
    private final UnicornPointer pointer;

    private MemoryBlockImpl(Memory memory, UnicornPointer pointer) {
        this.memory = memory;
        this.pointer = pointer;
    }

    @Override
    public UnicornPointer getPointer() {
        return pointer;
    }

    @Override
    public boolean isSame(Pointer pointer) {
        return this.pointer.equals(pointer);
    }

    @Override
    public void free(boolean runtime) {
        memory.munmap(pointer.peer, (int) pointer.getSize());
    }

}
