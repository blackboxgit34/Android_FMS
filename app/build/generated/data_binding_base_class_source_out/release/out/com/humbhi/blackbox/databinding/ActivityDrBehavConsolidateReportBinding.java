// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDrBehavConsolidateReportBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button btnAppy;

  @NonNull
  public final LinearLayout customDate;

  @NonNull
  public final AppCompatEditText etSearchBar;

  @NonNull
  public final LinearLayoutCompat llCustomDateRange;

  @NonNull
  public final TextView loadMore;

  @NonNull
  public final RecyclerView rvRecycler;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvCustom;

  @NonNull
  public final TextView tvEndDate;

  @NonNull
  public final TextView tvStartDate;

  @NonNull
  public final TextView tvToday;

  @NonNull
  public final TextView tvWeek;

  @NonNull
  public final TextView tvYesterday;

  private ActivityDrBehavConsolidateReportBinding(@NonNull RelativeLayout rootView,
      @NonNull Button btnAppy, @NonNull LinearLayout customDate,
      @NonNull AppCompatEditText etSearchBar, @NonNull LinearLayoutCompat llCustomDateRange,
      @NonNull TextView loadMore, @NonNull RecyclerView rvRecycler,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull TextView tvCustom,
      @NonNull TextView tvEndDate, @NonNull TextView tvStartDate, @NonNull TextView tvToday,
      @NonNull TextView tvWeek, @NonNull TextView tvYesterday) {
    this.rootView = rootView;
    this.btnAppy = btnAppy;
    this.customDate = customDate;
    this.etSearchBar = etSearchBar;
    this.llCustomDateRange = llCustomDateRange;
    this.loadMore = loadMore;
    this.rvRecycler = rvRecycler;
    this.toolbar = toolbar;
    this.tvCustom = tvCustom;
    this.tvEndDate = tvEndDate;
    this.tvStartDate = tvStartDate;
    this.tvToday = tvToday;
    this.tvWeek = tvWeek;
    this.tvYesterday = tvYesterday;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDrBehavConsolidateReportBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDrBehavConsolidateReportBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_dr_behav_consolidate_report, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDrBehavConsolidateReportBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAppy;
      Button btnAppy = ViewBindings.findChildViewById(rootView, id);
      if (btnAppy == null) {
        break missingId;
      }

      id = R.id.customDate;
      LinearLayout customDate = ViewBindings.findChildViewById(rootView, id);
      if (customDate == null) {
        break missingId;
      }

      id = R.id.etSearchBar;
      AppCompatEditText etSearchBar = ViewBindings.findChildViewById(rootView, id);
      if (etSearchBar == null) {
        break missingId;
      }

      id = R.id.llCustomDateRange;
      LinearLayoutCompat llCustomDateRange = ViewBindings.findChildViewById(rootView, id);
      if (llCustomDateRange == null) {
        break missingId;
      }

      id = R.id.loadMore;
      TextView loadMore = ViewBindings.findChildViewById(rootView, id);
      if (loadMore == null) {
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

      id = R.id.tvToday;
      TextView tvToday = ViewBindings.findChildViewById(rootView, id);
      if (tvToday == null) {
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

      return new ActivityDrBehavConsolidateReportBinding((RelativeLayout) rootView, btnAppy,
          customDate, etSearchBar, llCustomDateRange, loadMore, rvRecycler, binding_toolbar,
          tvCustom, tvEndDate, tvStartDate, tvToday, tvWeek, tvYesterday);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}