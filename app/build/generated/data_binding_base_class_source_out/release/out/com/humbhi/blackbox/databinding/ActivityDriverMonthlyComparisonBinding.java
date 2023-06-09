// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.mikephil.charting.charts.BarChart;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDriverMonthlyComparisonBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button btnAppy;

  @NonNull
  public final Button btnGetComparison;

  @NonNull
  public final BarChart compChart;

  @NonNull
  public final LinearLayout customDate;

  @NonNull
  public final LinearLayoutCompat llCustomDateRange;

  @NonNull
  public final LinearLayoutCompat mainLayout;

  @NonNull
  public final ProgressBarBinding progressLayout;

  @NonNull
  public final AutoCompleteTextView spVehicles;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvCustom;

  @NonNull
  public final TextView tvEndDate;

  @NonNull
  public final TextView tvStartDate;

  private ActivityDriverMonthlyComparisonBinding(@NonNull RelativeLayout rootView,
      @NonNull Button btnAppy, @NonNull Button btnGetComparison, @NonNull BarChart compChart,
      @NonNull LinearLayout customDate, @NonNull LinearLayoutCompat llCustomDateRange,
      @NonNull LinearLayoutCompat mainLayout, @NonNull ProgressBarBinding progressLayout,
      @NonNull AutoCompleteTextView spVehicles, @NonNull ToolbarLayoutBinding toolbar,
      @NonNull TextView tvCustom, @NonNull TextView tvEndDate, @NonNull TextView tvStartDate) {
    this.rootView = rootView;
    this.btnAppy = btnAppy;
    this.btnGetComparison = btnGetComparison;
    this.compChart = compChart;
    this.customDate = customDate;
    this.llCustomDateRange = llCustomDateRange;
    this.mainLayout = mainLayout;
    this.progressLayout = progressLayout;
    this.spVehicles = spVehicles;
    this.toolbar = toolbar;
    this.tvCustom = tvCustom;
    this.tvEndDate = tvEndDate;
    this.tvStartDate = tvStartDate;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDriverMonthlyComparisonBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDriverMonthlyComparisonBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_driver_monthly_comparison, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDriverMonthlyComparisonBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAppy;
      Button btnAppy = ViewBindings.findChildViewById(rootView, id);
      if (btnAppy == null) {
        break missingId;
      }

      id = R.id.btnGetComparison;
      Button btnGetComparison = ViewBindings.findChildViewById(rootView, id);
      if (btnGetComparison == null) {
        break missingId;
      }

      id = R.id.compChart;
      BarChart compChart = ViewBindings.findChildViewById(rootView, id);
      if (compChart == null) {
        break missingId;
      }

      id = R.id.customDate;
      LinearLayout customDate = ViewBindings.findChildViewById(rootView, id);
      if (customDate == null) {
        break missingId;
      }

      id = R.id.llCustomDateRange;
      LinearLayoutCompat llCustomDateRange = ViewBindings.findChildViewById(rootView, id);
      if (llCustomDateRange == null) {
        break missingId;
      }

      id = R.id.main_layout;
      LinearLayoutCompat mainLayout = ViewBindings.findChildViewById(rootView, id);
      if (mainLayout == null) {
        break missingId;
      }

      id = R.id.progressLayout;
      View progressLayout = ViewBindings.findChildViewById(rootView, id);
      if (progressLayout == null) {
        break missingId;
      }
      ProgressBarBinding binding_progressLayout = ProgressBarBinding.bind(progressLayout);

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

      id = R.id.tvEndDate;
      TextView tvEndDate = ViewBindings.findChildViewById(rootView, id);
      if (tvEndDate == null) {
        break missingId;
      }

      id = R.id.tvStartDate;
      TextView tvStartDate = ViewBindings.findChildViewById(rootView, id);
      if (tvStartDate == null) {
        break missingId;
      }

      return new ActivityDriverMonthlyComparisonBinding((RelativeLayout) rootView, btnAppy,
          btnGetComparison, compChart, customDate, llCustomDateRange, mainLayout,
          binding_progressLayout, spVehicles, binding_toolbar, tvCustom, tvEndDate, tvStartDate);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
