23
https://raw.githubusercontent.com/WeBankFinTech/Exchangis/master/modules/service/src/main/java/com/webank/wedatasphere/exchangis/datasource/service/DataSourceOwnerService.java
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

package com.webank.wedatasphere.exchangis.datasource.service;

import com.webank.wedatasphere.exchangis.common.service.IBaseService;
import com.webank.wedatasphere.exchangis.datasource.domain.DataSourceOwner;

/**
 * Data source owner service
 * @author davidhua
 * 2020/4/5
 */
public interface DataSourceOwnerService extends IBaseService<DataSourceOwner> {

    /**
     * Select by name
     * @param ownerName
     * @return
     */
    DataSourceOwner getByName(String ownerName);
}
