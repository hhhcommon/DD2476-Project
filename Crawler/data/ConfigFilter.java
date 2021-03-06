2
https://raw.githubusercontent.com/Heemooo/Bored/master/src/main/java/com/bored/listen/ConfigFilter.java
package com.bored.listen;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

public class ConfigFilter implements IOFileFilter {
    @Override
    public boolean accept(File file) {
        return accept(file, file.getName());
    }

    @Override
    public boolean accept(File file, String name) {
        return "config.toml".equals(name);
    }
}
