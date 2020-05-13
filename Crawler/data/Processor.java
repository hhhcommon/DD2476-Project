23
https://raw.githubusercontent.com/WeBankFinTech/Exchangis/master/modules/executor/engine/datax/datax-core/src/main/java/com/webank/wedatasphere/exchangis/datax/core/processor/Processor.java
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

package com.webank.wedatasphere.exchangis.datax.core.processor;

import java.util.List;

/**
 * @author davidhua
 * 2019/8/21
 */
public interface Processor<T>  {
    /**
     * process the column list
     * @param columnData
     * @exception
     * @return
     */
     T process(List<Object> columnData) throws Exception;
}