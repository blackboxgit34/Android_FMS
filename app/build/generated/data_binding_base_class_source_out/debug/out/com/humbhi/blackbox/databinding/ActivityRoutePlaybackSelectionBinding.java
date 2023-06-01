// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRoutePlaybackSelectionBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final Button bt24Hour;

  @NonNull
  public final Button btCustom;

  @NonNull
  public final Button btToday;

  @NonNull
  public final Button btnGetRoute;

  @NonNull
  public final LinearLayout customDate;

  @NonNull
  public final Spinner spVehicles;

  @NonNull
  public final CheckBox switch1;

  @NonNull
  public final CheckBox switch2;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvEndDate;

  @NonNull
  public final TextView tvEndTime;

  @NonNull
  public final TextView tvStartDate;

  @NonNull
  public final TextView tvStartTime;

  private ActivityRoutePlaybackSelectionBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull Button bt24Hour, @NonNull Button btCustom, @NonNull Button btToday,
      @NonNull Button btnGetRoute, @NonNull LinearLayout customDate, @NonNull Spinner spVehicles,
      @NonNull CheckBox switch1, @NonNull CheckBox switch2, @NonNull ToolbarLayoutBinding toolbar,
      @NonNull TextView tvEndDate, @NonNull TextView tvEndTime, @NonNull TextView tvStartDate,
      @NonNull TextView tvStartTime) {
    this.rootView = rootView;
    this.bt24Hour = bt24Hour;
    this.btCustom = btCustom;
    this.btToday = btToday;
    this.btnGetRoute = btnGetRoute;
    this.customDate = customDate;
    this.spVehicles = spVehicles;
    this.switch1 = switch1;
    this.switch2 = switch2;
    this.toolbar = toolbar;
    this.tvEndDate = tvEndDate;
    this.tvEndTime = tvEndTime;
    this.tvStartDate = tvStartDate;
    this.tvStartTime = tvStartTime;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRoutePlaybackSelectionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRoutePlaybackSelectionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_route_playback_selection, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRoutePlaybackSelectionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bt24Hour;
      Button bt24Hour = ViewBindings.findChildViewById(rootView, id);
      if (bt24Hour == null) {
        break missingId;
      }

      id = R.id.btCustom;
      Button btCustom = ViewBindings.findChildViewById(rootView, id);
      if (btCustom == null) {
        break missingId;
      }

      id = R.id.btToday;
      Button btToday = ViewBindings.findChildViewById(rootView, id);
      if (btToday == null) {
        break missingId;
      }

      id = R.id.btnGetRoute;
      Button btnGetRoute = ViewBindings.findChildViewById(rootView, id);
      if (btnGetRoute == null) {
        break missingId;
      }

      id = R.id.customDate;
      LinearLayout customDate = ViewBindings.findChildViewById(rootView, id);
      if (customDate == null) {
        break missingId;
      }

      id = R.id.spVehicles;
      Spinner spVehicles = ViewBindings.findChildViewById(rootView, id);
      if (spVehicles == null) {
        break missingId;
      }

      id = R.id.switch1;
      CheckBox switch1 = ViewBindings.findChildViewById(rootView, id);
      if (switch1 == null) {
        break missingId;
      }

      id = R.id.switch2;
      CheckBox switch2 = ViewBindings.findChildViewById(rootView, id);
      if (switch2 == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvEndDate;
      TextView tvEndDate = ViewBindings.findChildViewById(rootView, id);
      if (tvEndDate == null) {
        break missingId;
      }

      id = R.id.tvEndTime;
      TextView tvEndTime = ViewBindings.findChildViewById(rootView, id);
      if (tvEndTime == null) {
        break missingId;
      }

      id = R.id.tvStartDate;
      TextView tvStartDate = ViewBindings.findChildViewById(rootView, id);
      if (tvStartDate == null) {
        break missingId;
      }

      id = R.id.tvStartTime;
      TextView tvStartTime = ViewBindings.findChildViewById(rootView, id);
      if (tvStartTime == null) {
        break missingId;
      }

      return new ActivityRoutePlaybackSelectionBinding((LinearLayoutCompat) rootView, bt24Hour,
          btCustom, btToday, btnGetRoute, customDate, spVehicles, switch1, switch2, binding_toolbar,
          tvEndDate, tvEndTime, tvStartDate, tvStartTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}