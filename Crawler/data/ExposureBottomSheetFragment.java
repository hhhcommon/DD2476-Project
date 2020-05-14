205
https://raw.githubusercontent.com/google/exposure-notifications-android/master/app/src/main/java/com/google/android/apps/exposurenotification/activities/ExposureBottomSheetFragment.java
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

package com.google.android.apps.exposurenotification.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.apps.exposurenotification.R;
import com.google.android.apps.exposurenotification.common.StringUtils;
import com.google.android.apps.exposurenotification.storage.ExposureEntity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.Locale;
import org.threeten.bp.Instant;
import org.threeten.bp.temporal.ChronoUnit;

public class ExposureBottomSheetFragment extends BottomSheetDialogFragment {

  static final String TAG = "ExposureDetails";
  private static final String KEY_DATE_MILLIS_SINCE_EPOCH = "KEY_DATE_MILLIS_SINCE_EPOCH";

  static ExposureBottomSheetFragment newInstance(ExposureEntity exposureEntity) {
    ExposureBottomSheetFragment fragment = new ExposureBottomSheetFragment();
    Bundle args = new Bundle();
    args.putLong(KEY_DATE_MILLIS_SINCE_EPOCH, exposureEntity.getDateMillisSinceEpoch());
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.exposure_bottom_sheet, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    TextView possibleExposureSubheading = view.findViewById(R.id.possible_exposure_subheading);
    TextView verifiedResultExplanation = view.findViewById(R.id.verified_result_explanation);
    Button learnMore = view.findViewById(R.id.learn_more_button);
    Button done = view.findViewById(R.id.done_button);

    if (getArguments() != null) {
      long dateMillisSinceEpoch = getArguments().getLong(KEY_DATE_MILLIS_SINCE_EPOCH);
      verifiedResultExplanation.setText(getFormattedResultsExplanation(dateMillisSinceEpoch));
      possibleExposureSubheading
          .setText(getFormattedPossibleExposureSubheading(dateMillisSinceEpoch));
    }

    learnMore.setOnClickListener((v) -> learnMoreClicked());
    done.setOnClickListener((v) -> doneClicked());
  }

  private void learnMoreClicked() {
    startActivity(new Intent(Intent.ACTION_VIEW)
        .setData(Uri.parse(getString(R.string.verified_result_learn_more_href)))
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
  }

  private void doneClicked() {
    dismiss();
  }

  @NonNull
  private String getFormattedPossibleExposureSubheading(long dataMillisSinceEpoch) {
    Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
    Instant then = Instant.ofEpochMilli(dataMillisSinceEpoch);

    int daysAgo = (int) ChronoUnit.DAYS.between(then, now);

    return getResources().getQuantityString(
        R.plurals.possible_exposure_subheading, daysAgo, daysAgo);
  }

  @NonNull
  private String getFormattedResultsExplanation(long dataMillisSinceEpoch) {
    Locale locale = getResources().getConfiguration().locale;
    String formattedDate = StringUtils.timestampMsToMediumString(dataMillisSinceEpoch, locale);

    return getString(R.string.verified_result_explanation,
        formattedDate);
  }
}
