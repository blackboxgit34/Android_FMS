// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.anastr.speedviewlib.ImageLinearGauge;
import com.github.mikephil.charting.charts.LineChart;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFuelGraphJavaBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final Button btnAppy;

  @NonNull
  public final LineChart chart1;

  @NonNull
  public final LinearLayout customDate;

  @NonNull
  public final ImageLinearGauge gauge;

  @NonNull
  public final LinearLayoutCompat llCustomDateRange;

  @NonNull
  public final LinearLayoutCompat searchLayout;

  @NonNull
  public final AutoCompleteTextView spVehicles;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvCustom;

  @NonNull
  public final TextView tvDataDate;

  @NonNull
  public final TextView tvEmptyFuel;

  @NonNull
  public final TextView tvEndDate;

  @NonNull
  public final TextView tvFillingFuelLevel;

  @NonNull
  public final TextView tvFuelinTank;

  @NonNull
  public final TextView tvSelectVehicle;

  @NonNull
  public final TextView tvSelectedDateTime;

  @NonNull
  public final TextView tvSelectedDistance;

  @NonNull
  public final TextView tvSelectedFuel;

  @NonNull
  public final TextView tvSelectedLocation;

  @NonNull
  public final TextView tvSelectedSpeed;

  @NonNull
  public final TextView tvStartDate;

  @NonNull
  public final TextView tvToday;

  @NonNull
  public final TextView tvTotalTankVolume;

  @NonNull
  public final TextView tvYesterday;

  private ActivityFuelGraphJavaBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull Button btnAppy, @NonNull LineChart chart1, @NonNull LinearLayout customDate,
      @NonNull ImageLinearGauge gauge, @NonNull LinearLayoutCompat llCustomDateRange,
      @NonNull LinearLayoutCompat searchLayout, @NonNull AutoCompleteTextView spVehicles,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull TextView tvCustom,
      @NonNull TextView tvDataDate, @NonNull TextView tvEmptyFuel, @NonNull TextView tvEndDate,
      @NonNull TextView tvFillingFuelLevel, @NonNull TextView tvFuelinTank,
      @NonNull TextView tvSelectVehicle, @NonNull TextView tvSelectedDateTime,
      @NonNull TextView tvSelectedDistance, @NonNull TextView tvSelectedFuel,
      @NonNull TextView tvSelectedLocation, @NonNull TextView tvSelectedSpeed,
      @NonNull TextView tvStartDate, @NonNull TextView tvToday, @NonNull TextView tvTotalTankVolume,
      @NonNull TextView tvYesterday) {
    this.rootView = rootView;
    this.btnAppy = btnAppy;
    this.chart1 = chart1;
    this.customDate = customDate;
    this.gauge = gauge;
    this.llCustomDateRange = llCustomDateRange;
    this.searchLayout = searchLayout;
    this.spVehicles = spVehicles;
    this.toolbar = toolbar;
    this.tvCustom = tvCustom;
    this.tvDataDate = tvDataDate;
    this.tvEmptyFuel = tvEmptyFuel;
    this.tvEndDate = tvEndDate;
    this.tvFillingFuelLevel = tvFillingFuelLevel;
    this.tvFuelinTank = tvFuelinTank;
    this.tvSelectVehicle = tvSelectVehicle;
    this.tvSelectedDateTime = tvSelectedDateTime;
    this.tvSelectedDistance = tvSelectedDistance;
    this.tvSelectedFuel = tvSelectedFuel;
    this.tvSelectedLocation = tvSelectedLocation;
    this.tvSelectedSpeed = tvSelectedSpeed;
    this.tvStartDate = tvStartDate;
    this.tvToday = tvToday;
    this.tvTotalTankVolume = tvTotalTankVolume;
    this.tvYesterday = tvYesterday;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFuelGraphJavaBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFuelGraphJavaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_fuel_graph_java, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFuelGraphJavaBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAppy;
      Button btnAppy = ViewBindings.findChildViewById(rootView, id);
      if (btnAppy == null) {
        break missingId;
      }

      id = R.id.chart1;
      LineChart chart1 = ViewBindings.findChildViewById(rootView, id);
      if (chart1 == null) {
        break missingId;
      }

      id = R.id.customDate;
      LinearLayout customDate = ViewBindings.findChildViewById(rootView, id);
      if (customDate == null) {
        break missingId;
      }

      id = R.id.gauge;
      ImageLinearGauge gauge = ViewBindings.findChildViewById(rootView, id);
      if (gauge == null) {
        break missingId;
      }

      id = R.id.llCustomDateRange;
      LinearLayoutCompat llCustomDateRange = ViewBindings.findChildViewById(rootView, id);
      if (llCustomDateRange == null) {
        break missingId;
      }

      id = R.id.searchLayout;
      LinearLayoutCompat searchLayout = ViewBindings.findChildViewById(rootView, id);
      if (searchLayout == null) {
        break missingId;
      }

      id = R.id.spVehicles;
      AutoCompleteTextView spVehicles = ViewBindings.findChildViewById(rootView, id);
      if (spVehicles == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvCustom;
      TextView tvCustom = ViewBindings.findChildViewById(rootView, id);
      if (tvCustom == null) {
        break missingId;
      }

      id = R.id.tvDataDate;
      TextView tvDataDate = ViewBindings.findChildViewById(rootView, id);
      if (tvDataDate == null) {
        break missingId;
      }

      id = R.id.tvEmptyFuel;
      TextView tvEmptyFuel = ViewBindings.findChildViewById(rootView, id);
      if (tvEmptyFuel == null) {
        break missingId;
      }

      id = R.id.tvEndDate;
      TextView tvEndDate = ViewBindings.findChildViewById(rootView, id);
      if (tvEndDate == null) {
        break missingId;
      }

      id = R.id.tvFillingFuelLevel;
      TextView tvFillingFuelLevel = ViewBindings.findChildViewById(rootView, id);
      if (tvFillingFuelLevel == null) {
        break missingId;
      }

      id = R.id.tvFuelinTank;
      TextView tvFuelinTank = ViewBindings.findChildViewById(rootView, id);
      if (tvFuelinTank == null) {
        break missingId;
      }

      id = R.id.tvSelectVehicle;
      TextView tvSelectVehicle = ViewBindings.findChildViewById(rootView, id);
      if (tvSelectVehicle == null) {
        break missingId;
      }

      id = R.id.tvSelectedDateTime;
      TextView tvSelectedDateTime = ViewBindings.findChildViewById(rootView, id);
      if (tvSelectedDateTime == null) {
        break missingId;
      }

      id = R.id.tvSelectedDistance;
      TextView tvSelectedDistance = ViewBindings.findChildViewById(rootView, id);
      if (tvSelectedDistance == null) {
        break missingId;
      }

      id = R.id.tvSelectedFuel;
      TextView tvSelectedFuel = ViewBindings.findChildViewById(rootView, id);
      if (tvSelectedFuel == null) {
        break missingId;
      }

      id = R.id.tvSelectedLocation;
      TextView tvSelectedLocation = ViewBindings.findChildViewById(rootView, id);
      if (tvSelectedLocation == null) {
        break missingId;
      }

      id = R.id.tvSelectedSpeed;
      TextView tvSelectedSpeed = ViewBindings.findChildViewById(rootView, id);
      if (tvSelectedSpeed == null) {
        break missingId;
      }

      id = R.id.tvStartDate;
      TextView tvStartDate = ViewBindings.findChildViewById(rootView, id);
      if (tvStartDate == null) {
        break missingId;
      }

      id = R.id.tvToday;
      TextView tvToday = ViewBindings.findChildViewById(rootView, id);
      if (tvToday == null) {
        break missingId;
      }

      id = R.id.tvTotalTankVolume;
      TextView tvTotalTankVolume = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalTankVolume == null) {
        break missingId;
      }

      id = R.id.tvYesterday;
      TextView tvYesterday = ViewBindings.findChildViewById(rootView, id);
      if (tvYesterday == null) {
        break missingId;
      }

      return new ActivityFuelGraphJavaBinding((LinearLayoutCompat) rootView, btnAppy, chart1,
          customDate, gauge, llCustomDateRange, searchLayout, spVehicles, binding_toolbar, tvCustom,
          tvDataDate, tvEmptyFuel, tvEndDate, tvFillingFuelLevel, tvFuelinTank, tvSelectVehicle,
          tvSelectedDateTime, tvSelectedDistance, tvSelectedFuel, tvSelectedLocation,
          tvSelectedSpeed, tvStartDate, tvToday, tvTotalTankVolume, tvYesterday);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
