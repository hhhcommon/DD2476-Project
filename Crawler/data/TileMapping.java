15
https://raw.githubusercontent.com/CodinGame/SpringChallenge2020/master/src/main/java/com/codingame/view/TileMapping.java
package com.codingame.view;

public class TileMapping {

    int mask;
    int value;

    public TileMapping(int value) {
        this(value, 0);
    }

    public TileMapping(int value, int mask) {
        this.mask = mask;
        this.value = value;
    }
}
