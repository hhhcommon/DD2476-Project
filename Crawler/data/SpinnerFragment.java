9
https://raw.githubusercontent.com/idanapp/IdanPlusPlus/master/app/src/main/java/com/example/idan/plusplus/ui/SpinnerFragment.java
package com.example.idan.plusplus.ui;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class SpinnerFragment extends Fragment {
    private static final String TAG = SpinnerFragment.class.getSimpleName();

    private static final int SPINNER_WIDTH = 100;
    private static final int SPINNER_HEIGHT = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProgressBar progressBar = new ProgressBar(container.getContext());
        if (container instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(SPINNER_WIDTH, SPINNER_HEIGHT,  Gravity.CENTER);
            progressBar.setBackgroundColor(Color.BLACK);
            progressBar.setLayoutParams(layoutParams);
        }
        return progressBar;
    }
}
