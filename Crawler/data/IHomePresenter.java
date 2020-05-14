9
https://raw.githubusercontent.com/TrillGates/TaobaoUnion/master/app/src/main/java/com/sunofbeaches/taobaounion/presenter/IHomePresenter.java
package com.sunofbeaches.taobaounion.presenter;

import com.sunofbeaches.taobaounion.base.IBasePresenter;
import com.sunofbeaches.taobaounion.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();
}
