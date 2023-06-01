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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMonthlyReportBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final AppCompatEditText etSearchBar;

  @NonNull
  public final TextView loadMore;

  @NonNull
  public final LinearLayoutCompat midLayout;

  @NonNull
  public final ProgressBarBinding progressLayout;

  @NonNull
  public final RecyclerView rvRecycler;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvBeforePrevious;

  @NonNull
  public final TextView tvCurrent;

  @NonNull
  public final TextView tvPrevious;

  private ActivityMonthlyReportBinding(@NonNull RelativeLayout rootView,
      @NonNull AppCompatEditText etSearchBar, @NonNull TextView loadMore,
      @NonNull LinearLayoutCompat midLayout, @NonNull ProgressBarBinding progressLayout,
      @NonNull RecyclerView rvRecycler, @NonNull ToolbarLayoutBinding toolbar,
      @NonNull TextView tvBeforePrevious, @NonNull TextView tvCurrent,
      @NonNull TextView tvPrevious) {
    this.rootView = rootView;
    this.etSearchBar = etSearchBar;
    this.loadMore = loadMore;
    this.midLayout = midLayout;
    this.progressLayout = progressLayout;
    this.rvRecycler = rvRecycler;
    this.toolbar = toolbar;
    this.tvBeforePrevious = tvBeforePrevious;
    this.tvCurrent = tvCurrent;
    this.tvPrevious = tvPrevious;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMonthlyReportBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMonthlyReportBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_monthly_report, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMonthlyReportBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.etSearchBar;
      AppCompatEditText etSearchBar = ViewBindings.findChildViewById(rootView, id);
      if (etSearchBar == null) {
        break missingId;
      }

      id = R.id.loadMore;
      TextView loadMore = ViewBindings.findChildViewById(rootView, id);
      if (loadMore == null) {
        break missingId;
      }

      id = R.id.midLayout;
      LinearLayoutCompat midLayout = ViewBindings.findChildViewById(rootView, id);
      if (midLayout == null) {
        break missingId;
      }

      id = R.id.progressLayout;
      View progressLayout = ViewBindings.findChildViewById(rootView, id);
      if (progressLayout == null) {
        break missingId;
      }
      ProgressBarBinding binding_progressLayout = ProgressBarBinding.bind(progressLayout);

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

      id = R.id.tvBeforePrevious;
      TextView tvBeforePrevious = ViewBindings.findChildViewById(rootView, id);
      if (tvBeforePrevious == null) {
        break missingId;
      }

      id = R.id.tvCurrent;
      TextView tvCurrent = ViewBindings.findChildViewById(rootView, id);
      if (tvCurrent == null) {
        break missingId;
      }

      id = R.id.tvPrevious;
      TextView tvPrevious = ViewBindings.findChildViewById(rootView, id);
      if (tvPrevious == null) {
        break missingId;
      }

      return new ActivityMonthlyReportBinding((RelativeLayout) rootView, etSearchBar, loadMore,
          midLayout, binding_progressLayout, rvRecycler, binding_toolbar, tvBeforePrevious,
          tvCurrent, tvPrevious);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
