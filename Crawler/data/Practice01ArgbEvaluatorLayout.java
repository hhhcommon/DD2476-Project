2
https://raw.githubusercontent.com/Geekholt/Practice-CustomView/master/app/src/main/java/com/geekholt/practiceui/view/lesson6/practice01/Practice01ArgbEvaluatorLayout.java
package com.geekholt.practiceui.view.lesson6.practice01;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.geekholt.practiceui.R;

public class Practice01ArgbEvaluatorLayout extends RelativeLayout {
    Practice01ArgbEvaluatorView view;
    Button animateBt;

    public Practice01ArgbEvaluatorLayout(Context context) {
        super(context);
    }

    public Practice01ArgbEvaluatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice01ArgbEvaluatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = (Practice01ArgbEvaluatorView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
                // 在这里使用 ObjectAnimator.setEvaluator() 来设置 ArgbEvaluator，修复闪烁问题
                animator.setInterpolator(new LinearInterpolator());
                animator.setEvaluator(new ArgbEvaluator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }
}
