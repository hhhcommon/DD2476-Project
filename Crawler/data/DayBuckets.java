125
https://raw.githubusercontent.com/DP-3T/dp3t-sdk-backend/develop/dpppt-backend-sdk/dpppt-backend-sdk-model/src/main/java/org/dpppt/backend/sdk/model/gaen/DayBuckets.java
package org.dpppt.backend.sdk.model.gaen;

import java.util.List;

public class DayBuckets {
    String day;
    List<String> relativeUrls;


    public String getDay() {
        return this.day;
    }

    public List<String> getRelativeUrls() {
        return this.relativeUrls;
    }

    public DayBuckets day(String day) {
        this.day = day;
        return this;
    }

    public DayBuckets relativeUrls(List<String> relativeUrls) {
        this.relativeUrls = relativeUrls;
        return this;
    }
    
}