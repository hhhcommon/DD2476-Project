125
https://raw.githubusercontent.com/DP-3T/dp3t-sdk-backend/develop/dpppt-backend-sdk/dpppt-backend-sdk-model/src/main/java/org/dpppt/backend/sdk/model/gaen/GaenSecondDay.java
package org.dpppt.backend.sdk.model.gaen;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class GaenSecondDay {
    @NotNull
    @Valid
    private GaenKey delayedKey;
    private Integer fake;


    public GaenKey getDelayedKey() {
        return this.delayedKey;
    }

    public void setDelayedKey(GaenKey delayedKey) {
        this.delayedKey = delayedKey;
    }

    public Integer getFake() {
        return this.fake;
    }

    public void setFake(Integer fake) {
        this.fake = fake;
    }

}