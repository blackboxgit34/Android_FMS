// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityGlocationOnMapBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final CardView cvDetails;

  @NonNull
  public final EditText etSearch;

  @NonNull
  public final ImageView icClose;

  @NonNull
  public final ImageView ivSearch;

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
  public final TextView mapView;

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
  public final RelativeLayout rlSearch;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final AppCompatTextView tvDataDate;

  @NonNull
  public final AppCompatTextView tvDistance;

  @NonNull
  public final TextView tvHighSpeedCount;

  @NonNull
  public final TextView tvIgnitionCount;

  @NonNull
  public final AppCompatTextView tvLocation;

  @NonNull
  public final TextView tvMovingCount;

  @NonNull
  public final TextView tvParkedVehicle;

  @NonNull
  public final AppCompatTextView tvProgressStatus;

  @NonNull
  public final AppCompatTextView tvSpeed;

  @NonNull
  public final TextView tvTotalVehicles;

  @NonNull
  public final TextView tvTowdCount;

  @NonNull
  public final AppCompatTextView tvUnits;

  @NonNull
  public final TextView tvUnreachCount;

  @NonNull
  public final AppCompatTextView tvVehicleName;

  private ActivityGlocationOnMapBinding(@NonNull RelativeLayout rootView,
      @NonNull CardView cvDetails, @NonNull EditText etSearch, @NonNull ImageView icClose,
      @NonNull ImageView ivSearch, @NonNull LinearLayout llHiSpeed, @NonNull LinearLayout llIdeal,
      @NonNull LinearLayout llInactive, @NonNull LinearLayout llRunning,
      @NonNull LinearLayout llStopped, @NonNull LinearLayout llTotal, @NonNull LinearLayout llTowed,
      @NonNull TextView mapView, @NonNull RadioButton rbHiSpeed, @NonNull RadioButton rbIgnition,
      @NonNull RadioButton rbMoving, @NonNull RadioButton rbParked, @NonNull RadioButton rbTotal,
      @NonNull RadioButton rbTowed, @NonNull RadioButton rbUnreach,
      @NonNull RelativeLayout rlSearch, @NonNull ToolbarLayoutBinding toolbar,
      @NonNull AppCompatTextView tvDataDate, @NonNull AppCompatTextView tvDistance,
      @NonNull TextView tvHighSpeedCount, @NonNull TextView tvIgnitionCount,
      @NonNull AppCompatTextView tvLocation, @NonNull TextView tvMovingCount,
      @NonNull TextView tvParkedVehicle, @NonNull AppCompatTextView tvProgressStatus,
      @NonNull AppCompatTextView tvSpeed, @NonNull TextView tvTotalVehicles,
      @NonNull TextView tvTowdCount, @NonNull AppCompatTextView tvUnits,
      @NonNull TextView tvUnreachCount, @NonNull AppCompatTextView tvVehicleName) {
    this.rootView = rootView;
    this.cvDetails = cvDetails;
    this.etSearch = etSearch;
    this.icClose = icClose;
    this.ivSearch = ivSearch;
    this.llHiSpeed = llHiSpeed;
    this.llIdeal = llIdeal;
    this.llInactive = llInactive;
    this.llRunning = llRunning;
    this.llStopped = llStopped;
    this.llTotal = llTotal;
    this.llTowed = llTowed;
    this.mapView = mapView;
    this.rbHiSpeed = rbHiSpeed;
    this.rbIgnition = rbIgnition;
    this.rbMoving = rbMoving;
    this.rbParked = rbParked;
    this.rbTotal = rbTotal;
    this.rbTowed = rbTowed;
    this.rbUnreach = rbUnreach;
    this.rlSearch = rlSearch;
    this.toolbar = toolbar;
    this.tvDataDate = tvDataDate;
    this.tvDistance = tvDistance;
    this.tvHighSpeedCount = tvHighSpeedCount;
    this.tvIgnitionCount = tvIgnitionCount;
    this.tvLocation = tvLocation;
    this.tvMovingCount = tvMovingCount;
    this.tvParkedVehicle = tvParkedVehicle;
    this.tvProgressStatus = tvProgressStatus;
    this.tvSpeed = tvSpeed;
    this.tvTotalVehicles = tvTotalVehicles;
    this.tvTowdCount = tvTowdCount;
    this.tvUnits = tvUnits;
    this.tvUnreachCount = tvUnreachCount;
    this.tvVehicleName = tvVehicleName;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityGlocationOnMapBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityGlocationOnMapBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_glocation_on_map, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityGlocationOnMapBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvDetails;
      CardView cvDetails = ViewBindings.findChildViewById(rootView, id);
      if (cvDetails == null) {
        break missingId;
      }

      id = R.id.etSearch;
      EditText etSearch = ViewBindings.findChildViewById(rootView, id);
      if (etSearch == null) {
        break missingId;
      }

      id = R.id.icClose;
      ImageView icClose = ViewBindings.findChildViewById(rootView, id);
      if (icClose == null) {
        break missingId;
      }

      id = R.id.ivSearch;
      ImageView ivSearch = ViewBindings.findChildViewById(rootView, id);
      if (ivSearch == null) {
        break missingId;
      }

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

      id = R.id.mapView;
      TextView mapView = ViewBindings.findChildViewById(rootView, id);
      if (mapView == null) {
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

      id = R.id.rlSearch;
      RelativeLayout rlSearch = ViewBindings.findChildViewById(rootView, id);
      if (rlSearch == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvDataDate;
      AppCompatTextView tvDataDate = ViewBindings.findChildViewById(rootView, id);
      if (tvDataDate == null) {
        break missingId;
      }

      id = R.id.tvDistance;
      AppCompatTextView tvDistance = ViewBindings.findChildViewById(rootView, id);
      if (tvDistance == null) {
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

      id = R.id.tvLocation;
      AppCompatTextView tvLocation = ViewBindings.findChildViewById(rootView, id);
      if (tvLocation == null) {
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

      id = R.id.tvProgressStatus;
      AppCompatTextView tvProgressStatus = ViewBindings.findChildViewById(rootView, id);
      if (tvProgressStatus == null) {
        break missingId;
      }

      id = R.id.tvSpeed;
      AppCompatTextView tvSpeed = ViewBindings.findChildViewById(rootView, id);
      if (tvSpeed == null) {
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

      id = R.id.tvUnits;
      AppCompatTextView tvUnits = ViewBindings.findChildViewById(rootView, id);
      if (tvUnits == null) {
        break missingId;
      }

      id = R.id.tvUnreachCount;
      TextView tvUnreachCount = ViewBindings.findChildViewById(rootView, id);
      if (tvUnreachCount == null) {
        break missingId;
      }

      id = R.id.tvVehicleName;
      AppCompatTextView tvVehicleName = ViewBindings.findChildViewById(rootView, id);
      if (tvVehicleName == null) {
        break missingId;
      }

      return new ActivityGlocationOnMapBinding((RelativeLayout) rootView, cvDetails, etSearch,
          icClose, ivSearch, llHiSpeed, llIdeal, llInactive, llRunning, llStopped, llTotal, llTowed,
          mapView, rbHiSpeed, rbIgnition, rbMoving, rbParked, rbTotal, rbTowed, rbUnreach, rlSearch,
          binding_toolbar, tvDataDate, tvDistance, tvHighSpeedCount, tvIgnitionCount, tvLocation,
          tvMovingCount, tvParkedVehicle, tvProgressStatus, tvSpeed, tvTotalVehicles, tvTowdCount,
          tvUnits, tvUnreachCount, tvVehicleName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
