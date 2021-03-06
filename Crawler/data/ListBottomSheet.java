2
https://raw.githubusercontent.com/aiqfome/aiqInput/master/aiqinput/src/main/java/com/aiqfome/aiqinput/ListBottomSheet.java
package com.aiqfome.aiqinput;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aiqfome.aiqinput.databinding.LayoutBottomSheetListBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ListBottomSheet extends BottomSheetDialogFragment {

    LayoutBottomSheetListBinding binding;

    private String title;
    private RecyclerView.Adapter adapter;

    public ListBottomSheet(String title, RecyclerView.Adapter adapter) {
        this.title = title;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = LayoutBottomSheetListBinding.inflate(inflater);
        binding.title.setText(title);

        binding.rvItems.setAdapter(adapter);
        binding.rvItems.setNestedScrollingEnabled(true);

        binding.rvItems.setLayoutManager(new LinearLayoutManager
                (getContext(), RecyclerView.VERTICAL, false));

        binding.executePendingBindings();
        return binding.getRoot();
    }

}
