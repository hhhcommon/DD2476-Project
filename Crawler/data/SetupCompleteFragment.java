13
https://raw.githubusercontent.com/CoboVault/cobo-vault-cold/master/app/src/main/java/com/cobo/cold/ui/fragment/setup/SetupCompleteFragment.java
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

import android.content.Intent;
import android.view.View;

import com.cobo.cold.R;
import com.cobo.cold.databinding.SetupCompleteBinding;
import com.cobo.cold.ui.MainActivity;

public class SetupCompleteFragment extends SetupVaultBaseFragment<SetupCompleteBinding> {
    @Override
    protected int setView() {
        return R.layout.setup_complete;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mBinding.complete.setOnClickListener(this::complete);
    }

    private void complete(View view) {
        Intent intent = new Intent(mActivity, MainActivity.class);
        startActivity(intent);
        mActivity.finish();
    }

}
