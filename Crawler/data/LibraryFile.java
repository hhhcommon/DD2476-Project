16
https://raw.githubusercontent.com/wmm1996528/unidbg_douyin10/master/src/main/java/com/github/unidbg/spi/LibraryFile.java
package com.github.unidbg.spi;

import com.github.unidbg.Emulator;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface LibraryFile {

    String getName();

    String getMapRegionName();

    LibraryFile resolveLibrary(Emulator<?> emulator, String soName) throws IOException;

    byte[] readToByteArray() throws IOException;

    ByteBuffer mapBuffer() throws IOException;

    String getPath();

}
