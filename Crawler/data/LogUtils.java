10
https://raw.githubusercontent.com/373675032/Molihub/master/src/utils/LogUtils.java
package utils;

import org.apache.log4j.Logger;

public class LogUtils {
    private static Logger logger;
    public static Logger getInstance(Class c){
        return logger =  Logger.getLogger(c);
    }
    private LogUtils(){}
}
