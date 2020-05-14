10
https://raw.githubusercontent.com/IzzyPrime/Admin/master/src/main/java/com/kalvin/kvf/modules/sys/vo/RoleMenuVO.java
package com.kalvin.kvf.modules.sys.vo;

import com.kalvin.kvf.modules.sys.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色带菜单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleMenuVO extends Role {
    private Long[] menuIds;
}
