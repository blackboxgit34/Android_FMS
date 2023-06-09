// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DrivingLimitAdapterBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CardView cvLiveStatus;

  @NonNull
  public final TextView tvDetails;

  @NonNull
  public final TextView tvDrivingCount;

  @NonNull
  public final TextView tvDrivingDuration;

  @NonNull
  public final TextView tvDrivingLimit;

  @NonNull
  public final TextView tvHaltLimit;

  @NonNull
  public final TextView tvVehNum;

  private DrivingLimitAdapterBinding(@NonNull LinearLayout rootView, @NonNull CardView cvLiveStatus,
      @NonNull TextView tvDetails, @NonNull TextView tvDrivingCount,
      @NonNull TextView tvDrivingDuration, @NonNull TextView tvDrivingLimit,
      @NonNull TextView tvHaltLimit, @NonNull TextView tvVehNum) {
    this.rootView = rootView;
    this.cvLiveStatus = cvLiveStatus;
    this.tvDetails = tvDetails;
    this.tvDrivingCount = tvDrivingCount;
    this.tvDrivingDuration = tvDrivingDuration;
    this.tvDrivingLimit = tvDrivingLimit;
    this.tvHaltLimit = tvHaltLimit;
    this.tvVehNum = tvVehNum;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DrivingLimitAdapterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DrivingLimitAdapterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.driving_limit_adapter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DrivingLimitAdapterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvLiveStatus;
      CardView cvLiveStatus = ViewBindings.findChildViewById(rootView, id);
      if (cvLiveStatus == null) {
        break missingId;
      }

      id = R.id.tvDetails;
      TextView tvDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvDetails == null) {
        break missingId;
      }

      id = R.id.tvDrivingCount;
      TextView tvDrivingCount = ViewBindings.findChildViewById(rootView, id);
      if (tvDrivingCount == null) {
        break missingId;
      }

      id = R.id.tvDrivingDuration;
      TextView tvDrivingDuration = ViewBindings.findChildViewById(rootView, id);
      if (tvDrivingDuration == null) {
        break missingId;
      }

      id = R.id.tvDrivingLimit;
      TextView tvDrivingLimit = ViewBindings.findChildViewById(rootView, id);
      if (tvDrivingLimit == null) {
        break missingId;
      }

      id = R.id.tvHaltLimit;
      TextView tvHaltLimit = ViewBindings.findChildViewById(rootView, id);
      if (tvHaltLimit == null) {
        break missingId;
      }

      id = R.id.tvVehNum;
      TextView tvVehNum = ViewBindings.findChildViewById(rootView, id);
      if (tvVehNum == null) {
        break missingId;
      }

      return new DrivingLimitAdapterBinding((LinearLayout) rootView, cvLiveStatus, tvDetails,
          tvDrivingCount, tvDrivingDuration, tvDrivingLimit, tvHaltLimit, tvVehNum);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
