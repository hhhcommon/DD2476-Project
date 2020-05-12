3
https://raw.githubusercontent.com/Silthus/sLimits/master/src/main/java/net/silthus/slib/config/typeconversions/StringTypeConversion.java
package net.silthus.slib.config.typeconversions;

import java.lang.reflect.Type;

/**
 * @author zml2008
 */
public class StringTypeConversion extends TypeConversion {

    @Override
    protected Object cast(Class<?> target, Type[] neededGenerics, Object value) {

        return value.toString();
    }

    @Override
    public boolean isApplicable(Class<?> target, Object value) {

        return String.class.isAssignableFrom(target);
    }

    @Override
    public int getParametersRequired() {

        return 0;
    }
}
