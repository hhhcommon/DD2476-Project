18
https://raw.githubusercontent.com/WeBankFinTech/Schedulis/master/azkaban-jobtype/src/main/java/com/webank/wedatasphere/schedulis/jobtype/hiveutils/util/AzkabanJobPropertyDescription.java
/*
 * Copyright 2020 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.wedatasphere.schedulis.jobtype.hiveutils.util;

import java.lang.annotation.Documented;

/**
 * Description of parameter passed to this class via the Azkaban property to
 * which the annotation is attached.
 */
@Documented
public @interface AzkabanJobPropertyDescription {
  // @TODO: Actually add the value in since it doesn't show up in the
  // javadoc... siargh.
  String value();
}
