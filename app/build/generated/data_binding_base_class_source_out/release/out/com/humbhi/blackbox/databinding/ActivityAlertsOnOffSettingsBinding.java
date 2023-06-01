// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAlertsOnOffSettingsBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final CardView cvBattery;

  @NonNull
  public final CardView cvGeofence;

  @NonNull
  public final CardView cvIgnition;

  @NonNull
  public final CardView cvOverStop;

  @NonNull
  public final CardView cvOverspeed;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final AppCompatTextView tvBattery;

  @NonNull
  public final AppCompatTextView tvFuelHeading;

  @NonNull
  public final AppCompatTextView tvGeofence;

  @NonNull
  public final AppCompatTextView tvTempHeading;

  @NonNull
  public final AppCompatTextView tvWorking;

  private ActivityAlertsOnOffSettingsBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull CardView cvBattery, @NonNull CardView cvGeofence, @NonNull CardView cvIgnition,
      @NonNull CardView cvOverStop, @NonNull CardView cvOverspeed,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull AppCompatTextView tvBattery,
      @NonNull AppCompatTextView tvFuelHeading, @NonNull AppCompatTextView tvGeofence,
      @NonNull AppCompatTextView tvTempHeading, @NonNull AppCompatTextView tvWorking) {
    this.rootView = rootView;
    this.cvBattery = cvBattery;
    this.cvGeofence = cvGeofence;
    this.cvIgnition = cvIgnition;
    this.cvOverStop = cvOverStop;
    this.cvOverspeed = cvOverspeed;
    this.toolbar = toolbar;
    this.tvBattery = tvBattery;
    this.tvFuelHeading = tvFuelHeading;
    this.tvGeofence = tvGeofence;
    this.tvTempHeading = tvTempHeading;
    this.tvWorking = tvWorking;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAlertsOnOffSettingsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAlertsOnOffSettingsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_alerts_on_off_settings, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAlertsOnOffSettingsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvBattery;
      CardView cvBattery = ViewBindings.findChildViewById(rootView, id);
      if (cvBattery == null) {
        break missingId;
      }

      id = R.id.cvGeofence;
      CardView cvGeofence = ViewBindings.findChildViewById(rootView, id);
      if (cvGeofence == null) {
        break missingId;
      }

      id = R.id.cvIgnition;
      CardView cvIgnition = ViewBindings.findChildViewById(rootView, id);
      if (cvIgnition == null) {
        break missingId;
      }

      id = R.id.cvOverStop;
      CardView cvOverStop = ViewBindings.findChildViewById(rootView, id);
      if (cvOverStop == null) {
        break missingId;
      }

      id = R.id.cvOverspeed;
      CardView cvOverspeed = ViewBindings.findChildViewById(rootView, id);
      if (cvOverspeed == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvBattery;
      AppCompatTextView tvBattery = ViewBindings.findChildViewById(rootView, id);
      if (tvBattery == null) {
        break missingId;
      }

      id = R.id.tvFuelHeading;
      AppCompatTextView tvFuelHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvFuelHeading == null) {
        break missingId;
      }

      id = R.id.tvGeofence;
      AppCompatTextView tvGeofence = ViewBindings.findChildViewById(rootView, id);
      if (tvGeofence == null) {
        break missingId;
      }

      id = R.id.tvTempHeading;
      AppCompatTextView tvTempHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvTempHeading == null) {
        break missingId;
      }

      id = R.id.tvWorking;
      AppCompatTextView tvWorking = ViewBindings.findChildViewById(rootView, id);
      if (tvWorking == null) {
        break missingId;
      }

      return new ActivityAlertsOnOffSettingsBinding((LinearLayoutCompat) rootView, cvBattery,
          cvGeofence, cvIgnition, cvOverStop, cvOverspeed, binding_toolbar, tvBattery,
          tvFuelHeading, tvGeofence, tvTempHeading, tvWorking);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}