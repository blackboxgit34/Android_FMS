// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public final class CustomDateLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final Button btnAppy;

  @NonNull
  public final LinearLayoutCompat customeDate;

  @NonNull
  public final TextView tvEndDate;

  @NonNull
  public final TextView tvEndTime;

  @NonNull
  public final TextView tvStartDate;

  @NonNull
  public final TextView tvStartTime;

  private CustomDateLayoutBinding(@NonNull LinearLayoutCompat rootView, @NonNull Button btnAppy,
      @NonNull LinearLayoutCompat customeDate, @NonNull TextView tvEndDate,
      @NonNull TextView tvEndTime, @NonNull TextView tvStartDate, @NonNull TextView tvStartTime) {
    this.rootView = rootView;
    this.btnAppy = btnAppy;
    this.customeDate = customeDate;
    this.tvEndDate = tvEndDate;
    this.tvEndTime = tvEndTime;
    this.tvStartDate = tvStartDate;
    this.tvStartTime = tvStartTime;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static CustomDateLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CustomDateLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.custom_date_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CustomDateLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAppy;
      Button btnAppy = ViewBindings.findChildViewById(rootView, id);
      if (btnAppy == null) {
        break missingId;
      }

      LinearLayoutCompat customeDate = (LinearLayoutCompat) rootView;

      id = R.id.tvEndDate;
      TextView tvEndDate = ViewBindings.findChildViewById(rootView, id);
      if (tvEndDate == null) {
        break missingId;
      }

      id = R.id.tvEndTime;
      TextView tvEndTime = ViewBindings.findChildViewById(rootView, id);
      if (tvEndTime == null) {
        break missingId;
      }

      id = R.id.tvStartDate;
      TextView tvStartDate = ViewBindings.findChildViewById(rootView, id);
      if (tvStartDate == null) {
        break missingId;
      }

      id = R.id.tvStartTime;
      TextView tvStartTime = ViewBindings.findChildViewById(rootView, id);
      if (tvStartTime == null) {
        break missingId;
      }

      return new CustomDateLayoutBinding((LinearLayoutCompat) rootView, btnAppy, customeDate,
          tvEndDate, tvEndTime, tvStartDate, tvStartTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
