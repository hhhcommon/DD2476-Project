2
https://raw.githubusercontent.com/aistairc/GeoFlink/master/src/main/java/GeoFlink/spatialObjects/SpatialObject.java
/*
Copyright 2020 Data Platform Research Team, AIRC, AIST, Japan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package GeoFlink.spatialObjects;

import org.apache.flink.api.common.functions.MapFunction;

import java.io.Serializable;

public class SpatialObject extends Object implements Serializable {

    //TODO: Assign timestamp
    public long timeStampMillisec;

    public SpatialObject() {}

}
