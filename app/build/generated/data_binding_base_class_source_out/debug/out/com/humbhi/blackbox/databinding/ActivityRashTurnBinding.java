// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.mikephil.charting.charts.BarChart;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRashTurnBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final BarChart DistanceChart;

  @NonNull
  public final AppCompatEditText etSearchBar;

  @NonNull
  public final CustomDateLayoutBinding llCustomDateRange;

  @NonNull
  public final TextView loadMore;

  @NonNull
  public final ProgressBarBinding progress;

  @NonNull
  public final RecyclerView rvChart;

  @NonNull
  public final RecyclerView rvRecycler;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvCustom;

  @NonNull
  public final TextView tvToday;

  @NonNull
  public final TextView tvTotalCount;

  @NonNull
  public final TextView tvWeek;

  @NonNull
  public final TextView tvYesterday;

  private ActivityRashTurnBinding(@NonNull RelativeLayout rootView, @NonNull BarChart DistanceChart,
      @NonNull AppCompatEditText etSearchBar, @NonNull CustomDateLayoutBinding llCustomDateRange,
      @NonNull TextView loadMore, @NonNull ProgressBarBinding progress,
      @NonNull RecyclerView rvChart, @NonNull RecyclerView rvRecycler,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull TextView tvCustom, @NonNull TextView tvToday,
      @NonNull TextView tvTotalCount, @NonNull TextView tvWeek, @NonNull TextView tvYesterday) {
    this.rootView = rootView;
    this.DistanceChart = DistanceChart;
    this.etSearchBar = etSearchBar;
    this.llCustomDateRange = llCustomDateRange;
    this.loadMore = loadMore;
    this.progress = progress;
    this.rvChart = rvChart;
    this.rvRecycler = rvRecycler;
    this.toolbar = toolbar;
    this.tvCustom = tvCustom;
    this.tvToday = tvToday;
    this.tvTotalCount = tvTotalCount;
    this.tvWeek = tvWeek;
    this.tvYesterday = tvYesterday;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRashTurnBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRashTurnBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_rash_turn, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRashTurnBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.DistanceChart;
      BarChart DistanceChart = ViewBindings.findChildViewById(rootView, id);
      if (DistanceChart == null) {
        break missingId;
      }

      id = R.id.etSearchBar;
      AppCompatEditText etSearchBar = ViewBindings.findChildViewById(rootView, id);
      if (etSearchBar == null) {
        break missingId;
      }

      id = R.id.llCustomDateRange;
      View llCustomDateRange = ViewBindings.findChildViewById(rootView, id);
      if (llCustomDateRange == null) {
        break missingId;
      }
      CustomDateLayoutBinding binding_llCustomDateRange = CustomDateLayoutBinding.bind(llCustomDateRange);

      id = R.id.loadMore;
      TextView loadMore = ViewBindings.findChildViewById(rootView, id);
      if (loadMore == null) {
        break missingId;
      }

      id = R.id.progress;
      View progress = ViewBindings.findChildViewById(rootView, id);
      if (progress == null) {
        break missingId;
      }
      ProgressBarBinding binding_progress = ProgressBarBinding.bind(progress);

      id = R.id.rvChart;
      RecyclerView rvChart = ViewBindings.findChildViewById(rootView, id);
      if (rvChart == null) {
        break missingId;
      }

      id = R.id.rvRecycler;
      RecyclerView rvRecycler = ViewBindings.findChildViewById(rootView, id);
      if (rvRecycler == null) {
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

      id = R.id.tvToday;
      TextView tvToday = ViewBindings.findChildViewById(rootView, id);
      if (tvToday == null) {
        break missingId;
      }

      id = R.id.tvTotalCount;
      TextView tvTotalCount = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalCount == null) {
        break missingId;
      }

      id = R.id.tvWeek;
      TextView tvWeek = ViewBindings.findChildViewById(rootView, id);
      if (tvWeek == null) {
        break missingId;
      }

      id = R.id.tvYesterday;
      TextView tvYesterday = ViewBindings.findChildViewById(rootView, id);
      if (tvYesterday == null) {
        break missingId;
      }

      return new ActivityRashTurnBinding((RelativeLayout) rootView, DistanceChart, etSearchBar,
          binding_llCustomDateRange, loadMore, binding_progress, rvChart, rvRecycler,
          binding_toolbar, tvCustom, tvToday, tvTotalCount, tvWeek, tvYesterday);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
