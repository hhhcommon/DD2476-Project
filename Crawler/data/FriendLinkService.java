137
https://raw.githubusercontent.com/201206030/novel-plus/master/novel-front/src/main/java/com/java2nb/novel/service/FriendLinkService.java
package com.java2nb.novel.service;


import com.java2nb.novel.entity.FriendLink;

import java.util.List;

/**
 * @author 11797
 */
public interface FriendLinkService {

    /**
     * 查询首页友情链接
     * @return 集合
     * */
    List<FriendLink> listIndexLink();
}
