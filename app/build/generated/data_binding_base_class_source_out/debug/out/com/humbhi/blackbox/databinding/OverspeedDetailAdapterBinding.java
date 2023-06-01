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

public final class OverspeedDetailAdapterBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CardView cvLiveStatus;

  @NonNull
  public final TextView tvDateTime;

  @NonNull
  public final TextView tvLocation;

  @NonNull
  public final TextView tvSpeed;

  private OverspeedDetailAdapterBinding(@NonNull LinearLayout rootView,
      @NonNull CardView cvLiveStatus, @NonNull TextView tvDateTime, @NonNull TextView tvLocation,
      @NonNull TextView tvSpeed) {
    this.rootView = rootView;
    this.cvLiveStatus = cvLiveStatus;
    this.tvDateTime = tvDateTime;
    this.tvLocation = tvLocation;
    this.tvSpeed = tvSpeed;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static OverspeedDetailAdapterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static OverspeedDetailAdapterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.overspeed_detail_adapter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static OverspeedDetailAdapterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvLiveStatus;
      CardView cvLiveStatus = ViewBindings.findChildViewById(rootView, id);
      if (cvLiveStatus == null) {
        break missingId;
      }

      id = R.id.tvDateTime;
      TextView tvDateTime = ViewBindings.findChildViewById(rootView, id);
      if (tvDateTime == null) {
        break missingId;
      }

      id = R.id.tvLocation;
      TextView tvLocation = ViewBindings.findChildViewById(rootView, id);
      if (tvLocation == null) {
        break missingId;
      }

      id = R.id.tvSpeed;
      TextView tvSpeed = ViewBindings.findChildViewById(rootView, id);
      if (tvSpeed == null) {
        break missingId;
      }

      return new OverspeedDetailAdapterBinding((LinearLayout) rootView, cvLiveStatus, tvDateTime,
          tvLocation, tvSpeed);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}