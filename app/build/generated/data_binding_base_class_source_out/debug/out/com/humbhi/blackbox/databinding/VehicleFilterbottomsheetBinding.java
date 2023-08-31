// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class VehicleFilterbottomsheetBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final Button idBtnDismiss;

  @NonNull
  public final RadioButton rbAll;

  @NonNull
  public final RadioButton rbBatteryDisconnection;

  @NonNull
  public final RadioButton rbHiSpeed;

  @NonNull
  public final RadioButton rbIgnitionOn;

  @NonNull
  public final RadioButton rbMoving;

  @NonNull
  public final RadioButton rbParked;

  @NonNull
  public final RadioButton rbUnreach;

  private VehicleFilterbottomsheetBinding(@NonNull CardView rootView, @NonNull Button idBtnDismiss,
      @NonNull RadioButton rbAll, @NonNull RadioButton rbBatteryDisconnection,
      @NonNull RadioButton rbHiSpeed, @NonNull RadioButton rbIgnitionOn,
      @NonNull RadioButton rbMoving, @NonNull RadioButton rbParked,
      @NonNull RadioButton rbUnreach) {
    this.rootView = rootView;
    this.idBtnDismiss = idBtnDismiss;
    this.rbAll = rbAll;
    this.rbBatteryDisconnection = rbBatteryDisconnection;
    this.rbHiSpeed = rbHiSpeed;
    this.rbIgnitionOn = rbIgnitionOn;
    this.rbMoving = rbMoving;
    this.rbParked = rbParked;
    this.rbUnreach = rbUnreach;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static VehicleFilterbottomsheetBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static VehicleFilterbottomsheetBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.vehicle_filterbottomsheet, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static VehicleFilterbottomsheetBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.idBtnDismiss;
      Button idBtnDismiss = ViewBindings.findChildViewById(rootView, id);
      if (idBtnDismiss == null) {
        break missingId;
      }

      id = R.id.rbAll;
      RadioButton rbAll = ViewBindings.findChildViewById(rootView, id);
      if (rbAll == null) {
        break missingId;
      }

      id = R.id.rbBatteryDisconnection;
      RadioButton rbBatteryDisconnection = ViewBindings.findChildViewById(rootView, id);
      if (rbBatteryDisconnection == null) {
        break missingId;
      }

      id = R.id.rbHiSpeed;
      RadioButton rbHiSpeed = ViewBindings.findChildViewById(rootView, id);
      if (rbHiSpeed == null) {
        break missingId;
      }

      id = R.id.rbIgnitionOn;
      RadioButton rbIgnitionOn = ViewBindings.findChildViewById(rootView, id);
      if (rbIgnitionOn == null) {
        break missingId;
      }

      id = R.id.rbMoving;
      RadioButton rbMoving = ViewBindings.findChildViewById(rootView, id);
      if (rbMoving == null) {
        break missingId;
      }

      id = R.id.rbParked;
      RadioButton rbParked = ViewBindings.findChildViewById(rootView, id);
      if (rbParked == null) {
        break missingId;
      }

      id = R.id.rbUnreach;
      RadioButton rbUnreach = ViewBindings.findChildViewById(rootView, id);
      if (rbUnreach == null) {
        break missingId;
      }

      return new VehicleFilterbottomsheetBinding((CardView) rootView, idBtnDismiss, rbAll,
          rbBatteryDisconnection, rbHiSpeed, rbIgnitionOn, rbMoving, rbParked, rbUnreach);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
