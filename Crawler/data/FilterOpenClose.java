12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/model/process/queue/FilterOpenClose.java
package ru.bgcrm.model.process.queue;

import ru.bgcrm.util.ParameterMap;

public class FilterOpenClose extends Filter {
    public static final String OPEN = "open";
    public static final String CLOSE = "close";

    private String defaultValue;

    public FilterOpenClose(int id, ParameterMap filter) {
        super(id, filter);
        defaultValue = filter.get("defaultValue");
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
