23
https://raw.githubusercontent.com/WeBankFinTech/Exchangis/master/modules/executor/engine/datax/datax-core/src/main/java/com/webank/wedatasphere/exchangis/datax/core/processor/loader/JavaMemoryClassObject.java
/*
 *
 *  Copyright 2020 WeBank
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.wedatasphere.exchangis.datax.core.processor.loader;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author davidhua
 * 2019/8/26
 */
public class JavaMemoryClassObject extends SimpleJavaFileObject {

    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public JavaMemoryClassObject(String name, Kind kind){
        super(URI.create("string:///" + name.replace('.', '/')),
                kind);
    }

    public byte[] getBytes(){
        return outputStream.toByteArray();
    }
    @Override
    public OutputStream openOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        outputStream.close();
    }
}