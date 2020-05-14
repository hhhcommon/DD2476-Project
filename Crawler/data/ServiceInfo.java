34
https://raw.githubusercontent.com/lzj-github/registry/master/api/src/main/java/cn/lzj/nacos/api/pojo/ServiceInfo.java
package cn.lzj.nacos.api.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceInfo {

    //服务的名字
    private String name;

    //健康集群列表
    private String clusters;

    //该服务下的实例列表
    private List<Instance> instances = new ArrayList<Instance>();
}
