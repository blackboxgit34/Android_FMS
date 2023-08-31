// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DocumentsRemindersBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView rvDetails;

  @NonNull
  public final TextView tvDetails;

  @NonNull
  public final TextView tvVehicleName;

  private DocumentsRemindersBinding(@NonNull LinearLayout rootView, @NonNull RecyclerView rvDetails,
      @NonNull TextView tvDetails, @NonNull TextView tvVehicleName) {
    this.rootView = rootView;
    this.rvDetails = rvDetails;
    this.tvDetails = tvDetails;
    this.tvVehicleName = tvVehicleName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DocumentsRemindersBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DocumentsRemindersBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.documents_reminders, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DocumentsRemindersBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rvDetails;
      RecyclerView rvDetails = ViewBindings.findChildViewById(rootView, id);
      if (rvDetails == null) {
        break missingId;
      }

      id = R.id.tvDetails;
      TextView tvDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvDetails == null) {
        break missingId;
      }

      id = R.id.tvVehicleName;
      TextView tvVehicleName = ViewBindings.findChildViewById(rootView, id);
      if (tvVehicleName == null) {
        break missingId;
      }

      return new DocumentsRemindersBinding((LinearLayout) rootView, rvDetails, tvDetails,
          tvVehicleName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
