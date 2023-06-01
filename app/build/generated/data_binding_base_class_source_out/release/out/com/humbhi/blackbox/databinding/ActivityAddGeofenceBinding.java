// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAddGeofenceBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final Button btDraw;

  @NonNull
  public final FrameLayout fBottomSheet;

  @NonNull
  public final FragmentContainerView map;

  @NonNull
  public final RelativeLayout rlFrame;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvReset;

  @NonNull
  public final TextView tvSaveFence;

  private ActivityAddGeofenceBinding(@NonNull LinearLayoutCompat rootView, @NonNull Button btDraw,
      @NonNull FrameLayout fBottomSheet, @NonNull FragmentContainerView map,
      @NonNull RelativeLayout rlFrame, @NonNull ToolbarLayoutBinding toolbar,
      @NonNull TextView tvReset, @NonNull TextView tvSaveFence) {
    this.rootView = rootView;
    this.btDraw = btDraw;
    this.fBottomSheet = fBottomSheet;
    this.map = map;
    this.rlFrame = rlFrame;
    this.toolbar = toolbar;
    this.tvReset = tvReset;
    this.tvSaveFence = tvSaveFence;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAddGeofenceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAddGeofenceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_add_geofence, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAddGeofenceBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btDraw;
      Button btDraw = ViewBindings.findChildViewById(rootView, id);
      if (btDraw == null) {
        break missingId;
      }

      id = R.id.fBottomSheet;
      FrameLayout fBottomSheet = ViewBindings.findChildViewById(rootView, id);
      if (fBottomSheet == null) {
        break missingId;
      }

      id = R.id.map;
      FragmentContainerView map = ViewBindings.findChildViewById(rootView, id);
      if (map == null) {
        break missingId;
      }

      id = R.id.rlFrame;
      RelativeLayout rlFrame = ViewBindings.findChildViewById(rootView, id);
      if (rlFrame == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvReset;
      TextView tvReset = ViewBindings.findChildViewById(rootView, id);
      if (tvReset == null) {
        break missingId;
      }

      id = R.id.tvSaveFence;
      TextView tvSaveFence = ViewBindings.findChildViewById(rootView, id);
      if (tvSaveFence == null) {
        break missingId;
      }

      return new ActivityAddGeofenceBinding((LinearLayoutCompat) rootView, btDraw, fBottomSheet,
          map, rlFrame, binding_toolbar, tvReset, tvSaveFence);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}