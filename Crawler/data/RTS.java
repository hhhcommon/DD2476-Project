4
https://raw.githubusercontent.com/kemusiro/jr100-emulator-v2/master/src/jp/asamomiji/assembler/RTS.java
/**
 * JR-100 Emulator Version 2
 *
 * Copyright (c) 2006-2020 Kenichi Miyata
 *
 * This software is released under the the MIT license
 * http://opensource.org/licenses/mit-license.php
 */
package jp.asamomiji.assembler;

public class RTS extends NonBranchInstruction {
    public RTS(int address) {
        super(address);
        mnemonic = "RTS";
    }
}
