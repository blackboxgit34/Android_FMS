// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class WorkingHourAdapterBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final CardView cvMainLayout;

  @NonNull
  public final ImageView ivDetails;

  @NonNull
  public final TextView tvIgnitionTimes;

  @NonNull
  public final TextView tvMinTemp;

  @NonNull
  public final AppCompatTextView tvVehicleName;

  @NonNull
  public final TextView tvViewDetails;

  @NonNull
  public final TextView tvWorkHours;

  private WorkingHourAdapterBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull CardView cvMainLayout, @NonNull ImageView ivDetails,
      @NonNull TextView tvIgnitionTimes, @NonNull TextView tvMinTemp,
      @NonNull AppCompatTextView tvVehicleName, @NonNull TextView tvViewDetails,
      @NonNull TextView tvWorkHours) {
    this.rootView = rootView;
    this.cvMainLayout = cvMainLayout;
    this.ivDetails = ivDetails;
    this.tvIgnitionTimes = tvIgnitionTimes;
    this.tvMinTemp = tvMinTemp;
    this.tvVehicleName = tvVehicleName;
    this.tvViewDetails = tvViewDetails;
    this.tvWorkHours = tvWorkHours;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static WorkingHourAdapterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static WorkingHourAdapterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.working_hour_adapter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static WorkingHourAdapterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvMainLayout;
      CardView cvMainLayout = ViewBindings.findChildViewById(rootView, id);
      if (cvMainLayout == null) {
        break missingId;
      }

      id = R.id.ivDetails;
      ImageView ivDetails = ViewBindings.findChildViewById(rootView, id);
      if (ivDetails == null) {
        break missingId;
      }

      id = R.id.tvIgnitionTimes;
      TextView tvIgnitionTimes = ViewBindings.findChildViewById(rootView, id);
      if (tvIgnitionTimes == null) {
        break missingId;
      }

      id = R.id.tvMinTemp;
      TextView tvMinTemp = ViewBindings.findChildViewById(rootView, id);
      if (tvMinTemp == null) {
        break missingId;
      }

      id = R.id.tvVehicleName;
      AppCompatTextView tvVehicleName = ViewBindings.findChildViewById(rootView, id);
      if (tvVehicleName == null) {
        break missingId;
      }

      id = R.id.tvViewDetails;
      TextView tvViewDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvViewDetails == null) {
        break missingId;
      }

      id = R.id.tvWorkHours;
      TextView tvWorkHours = ViewBindings.findChildViewById(rootView, id);
      if (tvWorkHours == null) {
        break missingId;
      }

      return new WorkingHourAdapterBinding((LinearLayoutCompat) rootView, cvMainLayout, ivDetails,
          tvIgnitionTimes, tvMinTemp, tvVehicleName, tvViewDetails, tvWorkHours);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
