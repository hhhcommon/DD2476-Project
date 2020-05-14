15
https://raw.githubusercontent.com/Florizt/RxMVVM/master/rxmvvmlib/src/main/java/com/rx/rxmvvmlib/binding/viewadapter/recyclerview/LineManagers.java
package com.rx.rxmvvmlib.binding.viewadapter.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class LineManagers {
    protected LineManagers() {
    }

    public interface LineManagerFactory {
        RecyclerView.ItemDecoration create(RecyclerView recyclerView);
    }


    public static LineManagerFactory both() {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.BOTH);
            }
        };
    }

    public static LineManagerFactory horizontal() {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.HORIZONTAL);
            }
        };
    }

    public static LineManagerFactory vertical() {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new DividerLine(recyclerView.getContext(), DividerLine.LineDrawMode.VERTICAL);
            }
        };
    }
}
