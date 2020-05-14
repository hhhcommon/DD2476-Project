13
https://raw.githubusercontent.com/CoboVault/cobo-vault-cold/master/coinlib/src/main/java/com/cobo/coinlib/coins/IOST/Iost.java
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

package com.cobo.coinlib.coins.IOST;

import com.cobo.coinlib.Util;
import com.cobo.coinlib.coins.AbsCoin;
import com.cobo.coinlib.coins.AbsDeriver;
import com.cobo.coinlib.coins.AbsTx;
import com.cobo.coinlib.exception.InvalidTransactionException;
import com.cobo.coinlib.interfaces.Coin;
import com.cobo.coinlib.utils.B58;
import com.cobo.coinlib.utils.Coins;

import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.util.encoders.Hex;

public class Iost extends AbsCoin {

    public Iost(Coin impl) {
        super(impl);
    }

    @Override
    public String coinCode() {
        return Coins.IOST.coinCode();
    }

    public static class Tx extends AbsTx {

        public Tx(JSONObject object, String coinCode) throws JSONException, InvalidTransactionException {
            super(object, coinCode);
        }

        @Override
        protected void parseMetaData() throws JSONException {
            from = metaData.getString("from");
            to = metaData.getString("to");
            amount = Double.parseDouble(metaData.getString("amount"));
            memo = metaData.optString("memo");
            fee = Double.parseDouble(metaData.optString("fee", "0"));
            tokenName = metaData.optString("tokenName", "iost").toUpperCase();
            if (!"IOST".equals(tokenName)) {
                isToken = true;
            }

        }

        @Override
        protected void checkHdPath() throws InvalidTransactionException {
            checkHdPath(hdPath, true);
        }
    }

    public static class Deriver extends AbsDeriver {
        @Override
        public String derive(String xPubKey, int changeIndex, int addrIndex) {
            String pubKeyHex = Util.pubKeyFromExtentPubKey(xPubKey).substring(2);
            return new B58().encodeToString(Hex.decode(pubKeyHex));
        }

        @Override
        public String derive(String xPubKey) {
            String pubKeyHex = Util.pubKeyFromExtentPubKey(xPubKey).substring(2);
            return new B58().encodeToString(Hex.decode(pubKeyHex));
        }
    }
}
