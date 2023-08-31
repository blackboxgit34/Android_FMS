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
import com.vaibhavlakhera.circularprogressview.CircularProgressView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class OverSpeedDbBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CardView cvLiveStatus;

  @NonNull
  public final CircularProgressView pvOStimes;

  @NonNull
  public final TextView tvDetails;

  @NonNull
  public final TextView tvMaxSpeed;

  @NonNull
  public final TextView tvOverspeedCount;

  @NonNull
  public final TextView tvVehNum;

  private OverSpeedDbBinding(@NonNull LinearLayout rootView, @NonNull CardView cvLiveStatus,
      @NonNull CircularProgressView pvOStimes, @NonNull TextView tvDetails,
      @NonNull TextView tvMaxSpeed, @NonNull TextView tvOverspeedCount,
      @NonNull TextView tvVehNum) {
    this.rootView = rootView;
    this.cvLiveStatus = cvLiveStatus;
    this.pvOStimes = pvOStimes;
    this.tvDetails = tvDetails;
    this.tvMaxSpeed = tvMaxSpeed;
    this.tvOverspeedCount = tvOverspeedCount;
    this.tvVehNum = tvVehNum;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static OverSpeedDbBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static OverSpeedDbBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.over_speed_db, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static OverSpeedDbBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvLiveStatus;
      CardView cvLiveStatus = ViewBindings.findChildViewById(rootView, id);
      if (cvLiveStatus == null) {
        break missingId;
      }

      id = R.id.pvOStimes;
      CircularProgressView pvOStimes = ViewBindings.findChildViewById(rootView, id);
      if (pvOStimes == null) {
        break missingId;
      }

      id = R.id.tvDetails;
      TextView tvDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvDetails == null) {
        break missingId;
      }

      id = R.id.tvMaxSpeed;
      TextView tvMaxSpeed = ViewBindings.findChildViewById(rootView, id);
      if (tvMaxSpeed == null) {
        break missingId;
      }

      id = R.id.tvOverspeedCount;
      TextView tvOverspeedCount = ViewBindings.findChildViewById(rootView, id);
      if (tvOverspeedCount == null) {
        break missingId;
      }

      id = R.id.tvVehNum;
      TextView tvVehNum = ViewBindings.findChildViewById(rootView, id);
      if (tvVehNum == null) {
        break missingId;
      }

      return new OverSpeedDbBinding((LinearLayout) rootView, cvLiveStatus, pvOStimes, tvDetails,
          tvMaxSpeed, tvOverspeedCount, tvVehNum);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
