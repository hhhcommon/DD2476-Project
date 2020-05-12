47572
https://raw.githubusercontent.com/spring-projects/spring-boot/master/spring-boot-tests/spring-boot-smoke-tests/spring-boot-smoke-test-actuator/src/main/java/smoketest/actuator/SampleLegacyEndpoint.java
/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smoketest.actuator;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "lega.cy")
public class SampleLegacyEndpoint {

	@ReadOperation
	public Map<String, String> example() {
		return Collections.singletonMap("legacy", "legacy");
	}

}
