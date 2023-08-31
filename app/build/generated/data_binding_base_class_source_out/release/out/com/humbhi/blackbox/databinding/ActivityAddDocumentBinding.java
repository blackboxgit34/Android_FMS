// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAddDocumentBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final EditText Remarks;

  @NonNull
  public final EditText ReminderDays;

  @NonNull
  public final EditText dueDate;

  @NonNull
  public final LinearLayout progress;

  @NonNull
  public final AutoCompleteTextView spRenewalType;

  @NonNull
  public final AutoCompleteTextView spVehicles;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvSubmit;

  private ActivityAddDocumentBinding(@NonNull RelativeLayout rootView, @NonNull EditText Remarks,
      @NonNull EditText ReminderDays, @NonNull EditText dueDate, @NonNull LinearLayout progress,
      @NonNull AutoCompleteTextView spRenewalType, @NonNull AutoCompleteTextView spVehicles,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull TextView tvSubmit) {
    this.rootView = rootView;
    this.Remarks = Remarks;
    this.ReminderDays = ReminderDays;
    this.dueDate = dueDate;
    this.progress = progress;
    this.spRenewalType = spRenewalType;
    this.spVehicles = spVehicles;
    this.toolbar = toolbar;
    this.tvSubmit = tvSubmit;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAddDocumentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAddDocumentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_add_document, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAddDocumentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Remarks;
      EditText Remarks = ViewBindings.findChildViewById(rootView, id);
      if (Remarks == null) {
        break missingId;
      }

      id = R.id.ReminderDays;
      EditText ReminderDays = ViewBindings.findChildViewById(rootView, id);
      if (ReminderDays == null) {
        break missingId;
      }

      id = R.id.dueDate;
      EditText dueDate = ViewBindings.findChildViewById(rootView, id);
      if (dueDate == null) {
        break missingId;
      }

      id = R.id.progress;
      LinearLayout progress = ViewBindings.findChildViewById(rootView, id);
      if (progress == null) {
        break missingId;
      }

      id = R.id.spRenewalType;
      AutoCompleteTextView spRenewalType = ViewBindings.findChildViewById(rootView, id);
      if (spRenewalType == null) {
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

      id = R.id.tvSubmit;
      TextView tvSubmit = ViewBindings.findChildViewById(rootView, id);
      if (tvSubmit == null) {
        break missingId;
      }

      return new ActivityAddDocumentBinding((RelativeLayout) rootView, Remarks, ReminderDays,
          dueDate, progress, spRenewalType, spVehicles, binding_toolbar, tvSubmit);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}