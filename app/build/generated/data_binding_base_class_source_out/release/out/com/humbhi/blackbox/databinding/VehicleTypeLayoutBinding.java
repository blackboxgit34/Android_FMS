// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public final class VehicleTypeLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final LinearLayout llHiSpeed;

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
  public final RadioButton rbHiSpeed;

  @NonNull
  public final RadioButton rbIgnition;

  @NonNull
  public final RadioButton rbMoving;

  @NonNull
  public final RadioButton rbParked;

  @NonNull
  public final RadioButton rbTotal;

  @NonNull
  public final RadioButton rbTowed;

  @NonNull
  public final RadioButton rbUnreach;

  @NonNull
  public final TextView tvHighSpeedCount;

  @NonNull
  public final TextView tvIgnitionCount;

  @NonNull
  public final TextView tvMovingCount;

  @NonNull
  public final TextView tvParkedVehicle;

  @NonNull
  public final TextView tvTotalVehicles;

  @NonNull
  public final TextView tvTowdCount;

  @NonNull
  public final TextView tvUnreachCount;

  private VehicleTypeLayoutBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull LinearLayout llHiSpeed, @NonNull LinearLayout llIdeal,
      @NonNull LinearLayout llInactive, @NonNull LinearLayout llRunning,
      @NonNull LinearLayout llStopped, @NonNull LinearLayout llTotal, @NonNull LinearLayout llTowed,
      @NonNull RadioButton rbHiSpeed, @NonNull RadioButton rbIgnition,
      @NonNull RadioButton rbMoving, @NonNull RadioButton rbParked, @NonNull RadioButton rbTotal,
      @NonNull RadioButton rbTowed, @NonNull RadioButton rbUnreach,
      @NonNull TextView tvHighSpeedCount, @NonNull TextView tvIgnitionCount,
      @NonNull TextView tvMovingCount, @NonNull TextView tvParkedVehicle,
      @NonNull TextView tvTotalVehicles, @NonNull TextView tvTowdCount,
      @NonNull TextView tvUnreachCount) {
    this.rootView = rootView;
    this.llHiSpeed = llHiSpeed;
    this.llIdeal = llIdeal;
    this.llInactive = llInactive;
    this.llRunning = llRunning;
    this.llStopped = llStopped;
    this.llTotal = llTotal;
    this.llTowed = llTowed;
    this.rbHiSpeed = rbHiSpeed;
    this.rbIgnition = rbIgnition;
    this.rbMoving = rbMoving;
    this.rbParked = rbParked;
    this.rbTotal = rbTotal;
    this.rbTowed = rbTowed;
    this.rbUnreach = rbUnreach;
    this.tvHighSpeedCount = tvHighSpeedCount;
    this.tvIgnitionCount = tvIgnitionCount;
    this.tvMovingCount = tvMovingCount;
    this.tvParkedVehicle = tvParkedVehicle;
    this.tvTotalVehicles = tvTotalVehicles;
    this.tvTowdCount = tvTowdCount;
    this.tvUnreachCount = tvUnreachCount;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static VehicleTypeLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static VehicleTypeLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.vehicle_type_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static VehicleTypeLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.llHiSpeed;
      LinearLayout llHiSpeed = ViewBindings.findChildViewById(rootView, id);
      if (llHiSpeed == null) {
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

      id = R.id.rbHiSpeed;
      RadioButton rbHiSpeed = ViewBindings.findChildViewById(rootView, id);
      if (rbHiSpeed == null) {
        break missingId;
      }

      id = R.id.rbIgnition;
      RadioButton rbIgnition = ViewBindings.findChildViewById(rootView, id);
      if (rbIgnition == null) {
        break missingId;
      }

      id = R.id.rbMoving;
      RadioButton rbMoving = ViewBindings.findChildViewById(rootView, id);
      if (rbMoving == null) {
        break missingId;
      }

      id = R.id.rbParked;
      RadioButton rbParked = ViewBindings.findChildViewById(rootView, id);
      if (rbParked == null) {
        break missingId;
      }

      id = R.id.rbTotal;
      RadioButton rbTotal = ViewBindings.findChildViewById(rootView, id);
      if (rbTotal == null) {
        break missingId;
      }

      id = R.id.rbTowed;
      RadioButton rbTowed = ViewBindings.findChildViewById(rootView, id);
      if (rbTowed == null) {
        break missingId;
      }

      id = R.id.rbUnreach;
      RadioButton rbUnreach = ViewBindings.findChildViewById(rootView, id);
      if (rbUnreach == null) {
        break missingId;
      }

      id = R.id.tvHighSpeedCount;
      TextView tvHighSpeedCount = ViewBindings.findChildViewById(rootView, id);
      if (tvHighSpeedCount == null) {
        break missingId;
      }

      id = R.id.tvIgnitionCount;
      TextView tvIgnitionCount = ViewBindings.findChildViewById(rootView, id);
      if (tvIgnitionCount == null) {
        break missingId;
      }

      id = R.id.tvMovingCount;
      TextView tvMovingCount = ViewBindings.findChildViewById(rootView, id);
      if (tvMovingCount == null) {
        break missingId;
      }

      id = R.id.tvParkedVehicle;
      TextView tvParkedVehicle = ViewBindings.findChildViewById(rootView, id);
      if (tvParkedVehicle == null) {
        break missingId;
      }

      id = R.id.tvTotalVehicles;
      TextView tvTotalVehicles = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalVehicles == null) {
        break missingId;
      }

      id = R.id.tvTowdCount;
      TextView tvTowdCount = ViewBindings.findChildViewById(rootView, id);
      if (tvTowdCount == null) {
        break missingId;
      }

      id = R.id.tvUnreachCount;
      TextView tvUnreachCount = ViewBindings.findChildViewById(rootView, id);
      if (tvUnreachCount == null) {
        break missingId;
      }

      return new VehicleTypeLayoutBinding((LinearLayoutCompat) rootView, llHiSpeed, llIdeal,
          llInactive, llRunning, llStopped, llTotal, llTowed, rbHiSpeed, rbIgnition, rbMoving,
          rbParked, rbTotal, rbTowed, rbUnreach, tvHighSpeedCount, tvIgnitionCount, tvMovingCount,
          tvParkedVehicle, tvTotalVehicles, tvTowdCount, tvUnreachCount);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
