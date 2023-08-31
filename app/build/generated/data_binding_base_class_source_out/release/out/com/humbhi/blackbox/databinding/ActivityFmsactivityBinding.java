// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFmsactivityBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final CardView cvDocumentReminders;

  @NonNull
  public final CardView cvServiceReminder;

  @NonNull
  public final CardView cvVehicleManagement;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  private ActivityFmsactivityBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull CardView cvDocumentReminders, @NonNull CardView cvServiceReminder,
      @NonNull CardView cvVehicleManagement, @NonNull ToolbarLayoutBinding toolbar) {
    this.rootView = rootView;
    this.cvDocumentReminders = cvDocumentReminders;
    this.cvServiceReminder = cvServiceReminder;
    this.cvVehicleManagement = cvVehicleManagement;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFmsactivityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFmsactivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_fmsactivity, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFmsactivityBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cvDocumentReminders;
      CardView cvDocumentReminders = ViewBindings.findChildViewById(rootView, id);
      if (cvDocumentReminders == null) {
        break missingId;
      }

      id = R.id.cvServiceReminder;
      CardView cvServiceReminder = ViewBindings.findChildViewById(rootView, id);
      if (cvServiceReminder == null) {
        break missingId;
      }

      id = R.id.cvVehicleManagement;
      CardView cvVehicleManagement = ViewBindings.findChildViewById(rootView, id);
      if (cvVehicleManagement == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      return new ActivityFmsactivityBinding((LinearLayoutCompat) rootView, cvDocumentReminders,
          cvServiceReminder, cvVehicleManagement, binding_toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}