205
https://raw.githubusercontent.com/google/exposure-notifications-android/master/app/src/test/java/com/google/android/apps/exposurenotification/ExposureNotificationApplicationTest.java
/*
 * Copyright 2020 Google LLC
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
 *
 */

package com.google.android.apps.exposurenotification;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests of {@link ExposureNotificationApplication}.
 */
@RunWith(AndroidJUnit4.class)
public class ExposureNotificationApplicationTest {

  private ExposureNotificationApplication application;

  @Before
  public void setUp() {
    application = ApplicationProvider.getApplicationContext();
  }

  @Test
  public void onCreate() {
    application.onCreate();
  }

  @Test
  public void onTerminate() {
    application.onTerminate();
  }

}