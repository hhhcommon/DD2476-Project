34
https://raw.githubusercontent.com/lzj-github/registry/master/api/src/main/java/cn/lzj/nacos/api/pojo/BeatInfo.java
package cn.lzj.nacos.api.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class BeatInfo {

    private String namespaceId;
    private int port;
    private String ip;
    private String serviceName;
    private String clusterName;
    private Map<String, String> metadata;
    //private volatile long period;

}
