9
https://raw.githubusercontent.com/TrillGates/TaobaoUnion/master/app/src/main/java/com/sunofbeaches/taobaounion/utils/ToastUtil.java
package com.sunofbeaches.taobaounion.utils;

import android.widget.Toast;

import com.sunofbeaches.taobaounion.base.BaseApplication;

public class ToastUtil {

    private static Toast sToast;

    public static void showToast(String tips) {
        if(sToast == null) {
            sToast = Toast.makeText(BaseApplication.getAppContext(),tips,Toast.LENGTH_SHORT);
        } else {
            sToast.setText(tips);
        }
        sToast.show();
    }
}
