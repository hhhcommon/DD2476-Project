16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/Emulator.java
package com.github.unidbg;

import com.github.unidbg.arm.context.RegisterContext;
import com.github.unidbg.debugger.Debugger;
import com.github.unidbg.debugger.DebuggerType;
import com.github.unidbg.file.FileSystem;
import com.github.unidbg.file.NewFileIO;
import com.github.unidbg.listener.TraceCodeListener;
import com.github.unidbg.listener.TraceReadListener;
import com.github.unidbg.listener.TraceWriteListener;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.memory.SvcMemory;
import com.github.unidbg.spi.*;
import unicorn.Unicorn;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * cpu emulator
 * Created by zhkl0228 on 2017/5/2.
 */

public interface Emulator<T extends NewFileIO> extends Closeable, Disassembler, ValuePair {

    int getPointerSize();

    boolean is64Bit();
    boolean is32Bit();

    int getPageAlign();

    /**
     * trace memory read
     */
    @SuppressWarnings("unused")
    Emulator<T> traceRead();
    Emulator<T> traceRead(long begin, long end);
    @SuppressWarnings("unused")
    Emulator<T> traceRead(long begin, long end, TraceReadListener listener);

    /**
     * trace memory write
     */
    @SuppressWarnings("unused")
    Emulator<T> traceWrite();
    Emulator<T> traceWrite(long begin, long end);
    @SuppressWarnings("unused")
    Emulator<T> traceWrite(long begin, long end, TraceWriteListener listener);

    /**
     * trace instruction
     * note: low performance
     */
    void traceCode();
    void traceCode(long begin, long end);
    void traceCode(long begin, long end, TraceCodeListener listener);

    /**
     * redirect trace out
     */
    @SuppressWarnings("unused")
    void redirectTrace(File outFile);

    @SuppressWarnings("unused")
    void runAsm(String...asm);

    Number[] eFunc(long begin, Number... arguments);

    void eInit(long begin, Number... arguments);

    Number eEntry(long begin, long sp);

    /**
     * emulate block
     * @param begin start address
     * @param until stop address
     */
    Unicorn eBlock(long begin, long until);

    /**
     * 是否正在运行
     */
    @SuppressWarnings("unused")
    boolean isRunning();

    /**
     * show all registers
     */
    void showRegs();

    /**
     * show registers
     */
    void showRegs(int... regs);

    Module loadLibrary(File libraryFile) throws IOException;
    Module loadLibrary(File libraryFile, boolean forceCallInit) throws IOException;

    Alignment align(long addr, long size);

    Memory getMemory();

    Unicorn getUnicorn();

    int getPid();

    String getProcessName();

    Debugger attach();

    Debugger attach(DebuggerType type);

    FileSystem<T> getFileSystem();

    SvcMemory getSvcMemory();

    SyscallHandler<T> getSyscallHandler();

    String getLibraryExtension();
    String getLibraryPath();
    @SuppressWarnings("unused")
    LibraryFile createURLibraryFile(URL url, String libName);

    Dlfcn getDlfcn();

    /**
     * @param timeout  Duration to emulate the code (in microseconds). When this value is 0, we will emulate the code in infinite time, until the code is finished.
     */
    void setTimeout(long timeout);

    <V extends RegisterContext> V getContext();

}
