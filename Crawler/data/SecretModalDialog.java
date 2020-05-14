13
https://raw.githubusercontent.com/CoboVault/cobo-vault-cold/master/app/src/main/java/com/cobo/cold/ui/modal/SecretModalDialog.java
/*
 * Copyright (c) 2020 Cobo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * in the file COPYING.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cobo.cold.ui.modal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.cobo.cold.R;
import com.cobo.cold.databinding.SecretModalBinding;


public class SecretModalDialog extends DialogFragment {

    public static SecretModalDialog newInstance() {
        return new SecretModalDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SecretModalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.secret_modal, null, false);
        binding.know.setOnClickListener(v -> dismiss());
        Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.dialog)
                .setView(binding.getRoot())
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
