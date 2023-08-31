// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class FuelFillingDetailLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final TextView Beforefueling;

  @NonNull
  public final TextView afterFueling;

  @NonNull
  public final TextView fillingEndTime;

  @NonNull
  public final TextView fillingStartTime;

  @NonNull
  public final LinearLayoutCompat fuelFillingLayout;

  @NonNull
  public final TextView fuelLevelText;

  @NonNull
  public final TextView tvEndFuel;

  @NonNull
  public final TextView tvEndTime;

  @NonNull
  public final TextView tvFillingFuelLevel;

  @NonNull
  public final TextView tvStartTime;

  @NonNull
  public final TextView tvstartFuel;

  private FuelFillingDetailLayoutBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull TextView Beforefueling, @NonNull TextView afterFueling,
      @NonNull TextView fillingEndTime, @NonNull TextView fillingStartTime,
      @NonNull LinearLayoutCompat fuelFillingLayout, @NonNull TextView fuelLevelText,
      @NonNull TextView tvEndFuel, @NonNull TextView tvEndTime,
      @NonNull TextView tvFillingFuelLevel, @NonNull TextView tvStartTime,
      @NonNull TextView tvstartFuel) {
    this.rootView = rootView;
    this.Beforefueling = Beforefueling;
    this.afterFueling = afterFueling;
    this.fillingEndTime = fillingEndTime;
    this.fillingStartTime = fillingStartTime;
    this.fuelFillingLayout = fuelFillingLayout;
    this.fuelLevelText = fuelLevelText;
    this.tvEndFuel = tvEndFuel;
    this.tvEndTime = tvEndTime;
    this.tvFillingFuelLevel = tvFillingFuelLevel;
    this.tvStartTime = tvStartTime;
    this.tvstartFuel = tvstartFuel;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static FuelFillingDetailLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FuelFillingDetailLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fuel_filling_detail_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FuelFillingDetailLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Beforefueling;
      TextView Beforefueling = ViewBindings.findChildViewById(rootView, id);
      if (Beforefueling == null) {
        break missingId;
      }

      id = R.id.afterFueling;
      TextView afterFueling = ViewBindings.findChildViewById(rootView, id);
      if (afterFueling == null) {
        break missingId;
      }

      id = R.id.fillingEndTime;
      TextView fillingEndTime = ViewBindings.findChildViewById(rootView, id);
      if (fillingEndTime == null) {
        break missingId;
      }

      id = R.id.fillingStartTime;
      TextView fillingStartTime = ViewBindings.findChildViewById(rootView, id);
      if (fillingStartTime == null) {
        break missingId;
      }

      id = R.id.fuelFillingLayout;
      LinearLayoutCompat fuelFillingLayout = ViewBindings.findChildViewById(rootView, id);
      if (fuelFillingLayout == null) {
        break missingId;
      }

      id = R.id.fuelLevelText;
      TextView fuelLevelText = ViewBindings.findChildViewById(rootView, id);
      if (fuelLevelText == null) {
        break missingId;
      }

      id = R.id.tvEndFuel;
      TextView tvEndFuel = ViewBindings.findChildViewById(rootView, id);
      if (tvEndFuel == null) {
        break missingId;
      }

      id = R.id.tvEndTime;
      TextView tvEndTime = ViewBindings.findChildViewById(rootView, id);
      if (tvEndTime == null) {
        break missingId;
      }

      id = R.id.tvFillingFuelLevel;
      TextView tvFillingFuelLevel = ViewBindings.findChildViewById(rootView, id);
      if (tvFillingFuelLevel == null) {
        break missingId;
      }

      id = R.id.tvStartTime;
      TextView tvStartTime = ViewBindings.findChildViewById(rootView, id);
      if (tvStartTime == null) {
        break missingId;
      }

      id = R.id.tvstartFuel;
      TextView tvstartFuel = ViewBindings.findChildViewById(rootView, id);
      if (tvstartFuel == null) {
        break missingId;
      }

      return new FuelFillingDetailLayoutBinding((LinearLayoutCompat) rootView, Beforefueling,
          afterFueling, fillingEndTime, fillingStartTime, fuelFillingLayout, fuelLevelText,
          tvEndFuel, tvEndTime, tvFillingFuelLevel, tvStartTime, tvstartFuel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
