13
https://raw.githubusercontent.com/CoboVault/cobo-vault-cold/master/app/src/main/java/com/cobo/cold/ui/fragment/setup/GenerateMnemonicFragment.java
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

package com.cobo.cold.ui.fragment.setup;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.cobo.cold.R;
import com.cobo.cold.databinding.CommonModalBinding;
import com.cobo.cold.databinding.GenerateMnemonicBinding;
import com.cobo.cold.mnemonic.MnemonicInputTable;
import com.cobo.cold.ui.modal.ModalDialog;
import com.cobo.cold.ui.modal.SecretModalDialog;

public class GenerateMnemonicFragment extends SetupVaultBaseFragment<GenerateMnemonicBinding> {

    private SecretModalDialog dialog;

    @Override
    protected int setView() {
        return R.layout.generate_mnemonic;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mBinding.toolbar.setNavigationOnClickListener(v -> navigateUp());
        viewModel.setMnemonicCount(MnemonicInputTable.TWEENTYFOUR);
        mBinding.table.setMnemonicNumber(MnemonicInputTable.TWEENTYFOUR);
        mBinding.table.setEditable(false);
        mBinding.confirmSaved.setOnClickListener(this::confirmInput);
        observeMnemonic();
    }

    private void confirmInput(View view) {
        ModalDialog dialog = new ModalDialog();
        CommonModalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity),
                R.layout.common_modal, null, false);
        dialog.setBinding(binding);
        binding.title.setText(R.string.please_confirm);
        binding.subTitle.setText(R.string.mnemonic_save_hint);
        binding.confirm.setText(R.string.confirm_to_verify_mnemonic);
        binding.confirm.setOnClickListener(v -> {
            dialog.dismiss();
            navigate(R.id.action_to_confirmMnemonicFragment);
        });
        binding.close.setOnClickListener(v -> dialog.dismiss());
        dialog.show(mActivity.getSupportFragmentManager(), "");
    }

    private void observeMnemonic() {
        viewModel.getRandomMnemonic().observe(this, s -> {
            if (TextUtils.isEmpty(s)) {
                return;
            }
            String[] words = s.split(" ");
            if (words.length != 24) {
                return;
            }

            if (dialog == null) {
                dialog = new SecretModalDialog();
            }

            if (dialog.getDialog() == null || !dialog.getDialog().isShowing()) {
                dialog.show(mActivity.getSupportFragmentManager(), "");
            }

            for (int i = 0; i < words.length; i++) {
                mBinding.table.getWordsList().get(i).set(words[i]);
            }
            mBinding.confirmSaved.setEnabled(true);
        });
        viewModel.generateRandomMnemonic();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.getRandomMnemonic().removeObservers(this);
    }
}