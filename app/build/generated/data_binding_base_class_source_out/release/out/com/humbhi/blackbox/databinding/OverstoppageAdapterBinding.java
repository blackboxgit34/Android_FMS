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

public final class OverstoppageAdapterBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CardView cvLiveStatus;

  @NonNull
  public final TextView tvStoppageCount;

  @NonNull
  public final TextView tvStoppageLimit;

  @NonNull
  public final TextView tvVehicleName;

  @NonNull
  public final TextView tvtime;

  private OverstoppageAdapterBinding(@NonNull LinearLayout rootView, @NonNull CardView cvLiveStatus,
      @NonNull TextView tvStoppageCount, @NonNull TextView tvStoppageLimit,
      @NonNull TextView tvVehicleName, @NonNull TextView tvtime) {
    this.rootView = rootView;
    this.cvLiveStatus = cvLiveStatus;
    this.tvStoppageCount = tvStoppageCount;
    this.tvStoppageLimit = tvStoppageLimit;
    this.tvVehicleName = tvVehicleName;
    this.tvtime = tvtime;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static OverstoppageAdapterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static OverstoppageAdapterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.overstoppage_adapter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static OverstoppageAdapterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvLiveStatus;
      CardView cvLiveStatus = ViewBindings.findChildViewById(rootView, id);
      if (cvLiveStatus == null) {
        break missingId;
      }

      id = R.id.tvStoppageCount;
      TextView tvStoppageCount = ViewBindings.findChildViewById(rootView, id);
      if (tvStoppageCount == null) {
        break missingId;
      }

      id = R.id.tvStoppageLimit;
      TextView tvStoppageLimit = ViewBindings.findChildViewById(rootView, id);
      if (tvStoppageLimit == null) {
        break missingId;
      }

      id = R.id.tvVehicleName;
      TextView tvVehicleName = ViewBindings.findChildViewById(rootView, id);
      if (tvVehicleName == null) {
        break missingId;
      }

      id = R.id.tvtime;
      TextView tvtime = ViewBindings.findChildViewById(rootView, id);
      if (tvtime == null) {
        break missingId;
      }

      return new OverstoppageAdapterBinding((LinearLayout) rootView, cvLiveStatus, tvStoppageCount,
          tvStoppageLimit, tvVehicleName, tvtime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
