// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class ActivityTemperatureDetailReportBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final AppCompatEditText etSearchBar;

  @NonNull
  public final ProgressBarBinding llCustomProgress;

  @NonNull
  public final LinearLayoutCompat llMainLayout;

  @NonNull
  public final RecyclerView rvTemperature;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  private ActivityTemperatureDetailReportBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull AppCompatEditText etSearchBar, @NonNull ProgressBarBinding llCustomProgress,
      @NonNull LinearLayoutCompat llMainLayout, @NonNull RecyclerView rvTemperature,
      @NonNull ToolbarLayoutBinding toolbar) {
    this.rootView = rootView;
    this.etSearchBar = etSearchBar;
    this.llCustomProgress = llCustomProgress;
    this.llMainLayout = llMainLayout;
    this.rvTemperature = rvTemperature;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityTemperatureDetailReportBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityTemperatureDetailReportBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_temperature_detail_report, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityTemperatureDetailReportBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.etSearchBar;
      AppCompatEditText etSearchBar = ViewBindings.findChildViewById(rootView, id);
      if (etSearchBar == null) {
        break missingId;
      }

      id = R.id.llCustomProgress;
      View llCustomProgress = ViewBindings.findChildViewById(rootView, id);
      if (llCustomProgress == null) {
        break missingId;
      }
      ProgressBarBinding binding_llCustomProgress = ProgressBarBinding.bind(llCustomProgress);

      id = R.id.llMainLayout;
      LinearLayoutCompat llMainLayout = ViewBindings.findChildViewById(rootView, id);
      if (llMainLayout == null) {
        break missingId;
      }

      id = R.id.rvTemperature;
      RecyclerView rvTemperature = ViewBindings.findChildViewById(rootView, id);
      if (rvTemperature == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      return new ActivityTemperatureDetailReportBinding((LinearLayoutCompat) rootView, etSearchBar,
          binding_llCustomProgress, llMainLayout, rvTemperature, binding_toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}