// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.anastr.speedviewlib.ImageLinearGauge;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FueltheftdetailadapterBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageLinearGauge gGauge;

  @NonNull
  public final AppCompatTextView tvAfterFilling;

  @NonNull
  public final AppCompatTextView tvBeforeDrainDate;

  @NonNull
  public final TextView tvDrainLocation;

  @NonNull
  public final TextView tvFirstFilling;

  @NonNull
  public final TextView tvLastFilling;

  @NonNull
  public final TextView tvTotalDrain;

  @NonNull
  public final TextView tvVehicleName;

  private FueltheftdetailadapterBinding(@NonNull LinearLayout rootView,
      @NonNull ImageLinearGauge gGauge, @NonNull AppCompatTextView tvAfterFilling,
      @NonNull AppCompatTextView tvBeforeDrainDate, @NonNull TextView tvDrainLocation,
      @NonNull TextView tvFirstFilling, @NonNull TextView tvLastFilling,
      @NonNull TextView tvTotalDrain, @NonNull TextView tvVehicleName) {
    this.rootView = rootView;
    this.gGauge = gGauge;
    this.tvAfterFilling = tvAfterFilling;
    this.tvBeforeDrainDate = tvBeforeDrainDate;
    this.tvDrainLocation = tvDrainLocation;
    this.tvFirstFilling = tvFirstFilling;
    this.tvLastFilling = tvLastFilling;
    this.tvTotalDrain = tvTotalDrain;
    this.tvVehicleName = tvVehicleName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FueltheftdetailadapterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FueltheftdetailadapterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fueltheftdetailadapter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FueltheftdetailadapterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.gGauge;
      ImageLinearGauge gGauge = ViewBindings.findChildViewById(rootView, id);
      if (gGauge == null) {
        break missingId;
      }

      id = R.id.tvAfterFilling;
      AppCompatTextView tvAfterFilling = ViewBindings.findChildViewById(rootView, id);
      if (tvAfterFilling == null) {
        break missingId;
      }

      id = R.id.tvBeforeDrainDate;
      AppCompatTextView tvBeforeDrainDate = ViewBindings.findChildViewById(rootView, id);
      if (tvBeforeDrainDate == null) {
        break missingId;
      }

      id = R.id.tvDrainLocation;
      TextView tvDrainLocation = ViewBindings.findChildViewById(rootView, id);
      if (tvDrainLocation == null) {
        break missingId;
      }

      id = R.id.tvFirstFilling;
      TextView tvFirstFilling = ViewBindings.findChildViewById(rootView, id);
      if (tvFirstFilling == null) {
        break missingId;
      }

      id = R.id.tvLastFilling;
      TextView tvLastFilling = ViewBindings.findChildViewById(rootView, id);
      if (tvLastFilling == null) {
        break missingId;
      }

      id = R.id.tvTotalDrain;
      TextView tvTotalDrain = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalDrain == null) {
        break missingId;
      }

      id = R.id.tvVehicleName;
      TextView tvVehicleName = ViewBindings.findChildViewById(rootView, id);
      if (tvVehicleName == null) {
        break missingId;
      }

      return new FueltheftdetailadapterBinding((LinearLayout) rootView, gGauge, tvAfterFilling,
          tvBeforeDrainDate, tvDrainLocation, tvFirstFilling, tvLastFilling, tvTotalDrain,
          tvVehicleName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}