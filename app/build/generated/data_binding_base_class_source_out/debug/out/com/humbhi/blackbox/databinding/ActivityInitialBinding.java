// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityInitialBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final EditText accessCode;

  @NonNull
  public final EditText amount;

  @NonNull
  public final EditText cancelUrl;

  @NonNull
  public final EditText currency;

  @NonNull
  public final EditText merchantId;

  @NonNull
  public final EditText orderId;

  @NonNull
  public final EditText redirectUrl;

  @NonNull
  public final EditText rsaUrl;

  private ActivityInitialBinding(@NonNull RelativeLayout rootView, @NonNull EditText accessCode,
      @NonNull EditText amount, @NonNull EditText cancelUrl, @NonNull EditText currency,
      @NonNull EditText merchantId, @NonNull EditText orderId, @NonNull EditText redirectUrl,
      @NonNull EditText rsaUrl) {
    this.rootView = rootView;
    this.accessCode = accessCode;
    this.amount = amount;
    this.cancelUrl = cancelUrl;
    this.currency = currency;
    this.merchantId = merchantId;
    this.orderId = orderId;
    this.redirectUrl = redirectUrl;
    this.rsaUrl = rsaUrl;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityInitialBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityInitialBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_initial, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityInitialBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.accessCode;
      EditText accessCode = ViewBindings.findChildViewById(rootView, id);
      if (accessCode == null) {
        break missingId;
      }

      id = R.id.amount;
      EditText amount = ViewBindings.findChildViewById(rootView, id);
      if (amount == null) {
        break missingId;
      }

      id = R.id.cancelUrl;
      EditText cancelUrl = ViewBindings.findChildViewById(rootView, id);
      if (cancelUrl == null) {
        break missingId;
      }

      id = R.id.currency;
      EditText currency = ViewBindings.findChildViewById(rootView, id);
      if (currency == null) {
        break missingId;
      }

      id = R.id.merchantId;
      EditText merchantId = ViewBindings.findChildViewById(rootView, id);
      if (merchantId == null) {
        break missingId;
      }

      id = R.id.orderId;
      EditText orderId = ViewBindings.findChildViewById(rootView, id);
      if (orderId == null) {
        break missingId;
      }

      id = R.id.redirectUrl;
      EditText redirectUrl = ViewBindings.findChildViewById(rootView, id);
      if (redirectUrl == null) {
        break missingId;
      }

      id = R.id.rsaUrl;
      EditText rsaUrl = ViewBindings.findChildViewById(rootView, id);
      if (rsaUrl == null) {
        break missingId;
      }

      return new ActivityInitialBinding((RelativeLayout) rootView, accessCode, amount, cancelUrl,
          currency, merchantId, orderId, redirectUrl, rsaUrl);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
