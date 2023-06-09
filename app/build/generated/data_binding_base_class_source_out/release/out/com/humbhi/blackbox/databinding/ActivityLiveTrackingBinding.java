// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLiveTrackingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout fBottomSheet;

  @NonNull
  public final LinearLayout llIdeal;

  @NonNull
  public final LinearLayout llInactive;

  @NonNull
  public final LinearLayout llRunning;

  @NonNull
  public final LinearLayout llStopped;

  @NonNull
  public final LinearLayout llTotal;

  @NonNull
  public final LinearLayout llTowed;

  @NonNull
  public final FragmentContainerView map;

  @NonNull
  public final RadioButton rbTotal;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvPlayback;

  private ActivityLiveTrackingBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout fBottomSheet, @NonNull LinearLayout llIdeal,
      @NonNull LinearLayout llInactive, @NonNull LinearLayout llRunning,
      @NonNull LinearLayout llStopped, @NonNull LinearLayout llTotal, @NonNull LinearLayout llTowed,
      @NonNull FragmentContainerView map, @NonNull RadioButton rbTotal,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull TextView tvPlayback) {
    this.rootView = rootView;
    this.fBottomSheet = fBottomSheet;
    this.llIdeal = llIdeal;
    this.llInactive = llInactive;
    this.llRunning = llRunning;
    this.llStopped = llStopped;
    this.llTotal = llTotal;
    this.llTowed = llTowed;
    this.map = map;
    this.rbTotal = rbTotal;
    this.toolbar = toolbar;
    this.tvPlayback = tvPlayback;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLiveTrackingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLiveTrackingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_live_tracking, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLiveTrackingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fBottomSheet;
      FrameLayout fBottomSheet = ViewBindings.findChildViewById(rootView, id);
      if (fBottomSheet == null) {
        break missingId;
      }

      id = R.id.llIdeal;
      LinearLayout llIdeal = ViewBindings.findChildViewById(rootView, id);
      if (llIdeal == null) {
        break missingId;
      }

      id = R.id.llInactive;
      LinearLayout llInactive = ViewBindings.findChildViewById(rootView, id);
      if (llInactive == null) {
        break missingId;
      }

      id = R.id.llRunning;
      LinearLayout llRunning = ViewBindings.findChildViewById(rootView, id);
      if (llRunning == null) {
        break missingId;
      }

      id = R.id.llStopped;
      LinearLayout llStopped = ViewBindings.findChildViewById(rootView, id);
      if (llStopped == null) {
        break missingId;
      }

      id = R.id.llTotal;
      LinearLayout llTotal = ViewBindings.findChildViewById(rootView, id);
      if (llTotal == null) {
        break missingId;
      }

      id = R.id.llTowed;
      LinearLayout llTowed = ViewBindings.findChildViewById(rootView, id);
      if (llTowed == null) {
        break missingId;
      }

      id = R.id.map;
      FragmentContainerView map = ViewBindings.findChildViewById(rootView, id);
      if (map == null) {
        break missingId;
      }

      id = R.id.rbTotal;
      RadioButton rbTotal = ViewBindings.findChildViewById(rootView, id);
      if (rbTotal == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvPlayback;
      TextView tvPlayback = ViewBindings.findChildViewById(rootView, id);
      if (tvPlayback == null) {
        break missingId;
      }

      return new ActivityLiveTrackingBinding((ConstraintLayout) rootView, fBottomSheet, llIdeal,
          llInactive, llRunning, llStopped, llTotal, llTowed, map, rbTotal, binding_toolbar,
          tvPlayback);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
