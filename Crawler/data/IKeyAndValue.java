15
https://raw.githubusercontent.com/Florizt/RxMVVM/master/rxmvvmlib/src/main/java/com/rx/rxmvvmlib/binding/viewadapter/spinner/IKeyAndValue.java
package com.rx.rxmvvmlib.binding.viewadapter.spinner;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 * <p>
 * 下拉Spinner控件的键值对, 实现该接口,返回key,value值, 在xml绑定List<IKeyAndValue>
 */
public interface IKeyAndValue {
    String getKey();

    String getValue();
}
