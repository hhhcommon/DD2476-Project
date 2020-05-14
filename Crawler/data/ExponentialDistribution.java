277
https://raw.githubusercontent.com/DP-3T/dp3t-app-android/develop/app/src/main/java/org/dpppt/android/app/util/ExponentialDistribution.java
/*
 * Copyright (c) 2020 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */

package org.dpppt.android.app.util;

import java.security.SecureRandom;

public class ExponentialDistribution {

	public static double sampleFromStandard() {
		SecureRandom random = new SecureRandom();
		return -Math.log(1.0d - random.nextDouble());
	}

}
