13
https://raw.githubusercontent.com/CoboVault/cobo-vault-cold/master/app/src/main/java/com/cobo/cold/callables/GetPasswordTokenCallable.java
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

package com.cobo.cold.callables;

import androidx.annotation.NonNull;

import com.cobo.cold.encryption.interfaces.CONSTANTS;
import com.cobo.cold.encryptioncore.base.Packet;
import com.cobo.cold.encryptioncore.base.Payload;

import java.util.concurrent.Callable;

public class GetPasswordTokenCallable implements Callable<String> {

    private final String password;

    public GetPasswordTokenCallable(@NonNull String password) {
        this.password = password;
    }

    @Override
    public String call() {
        final Callable<Packet> callable = new BlockingCallable(
                new Packet.Builder(CONSTANTS.METHODS.VERIFY_USER_PASSWORD)
                        .addHexPayload(CONSTANTS.TAGS.CURRENT_PASSWORD, password)
                        .addBytePayload(CONSTANTS.TAGS.NEED_TOKEN, 1)
                        .build());
        try {
            Packet packet = callable.call();
            final Payload payload = packet.getPayload(CONSTANTS.TAGS.AUTH_TOKEN);
            if (payload != null) {
                return payload.toHex();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
