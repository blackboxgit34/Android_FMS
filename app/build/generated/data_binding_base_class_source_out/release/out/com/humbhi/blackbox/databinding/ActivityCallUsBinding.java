// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCallUsBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final LinearLayoutCompat llMainLayout;

  @NonNull
  public final ProgressBarBinding llProgressLayout;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final AppCompatTextView tvAccPersonContact;

  @NonNull
  public final AppCompatTextView tvAccPersonName;

  @NonNull
  public final TextView tvCallAccount;

  @NonNull
  public final TextView tvCallCust;

  @NonNull
  public final TextView tvCallSales;

  @NonNull
  public final AppCompatTextView tvCustContact;

  @NonNull
  public final AppCompatTextView tvCustName;

  @NonNull
  public final AppCompatTextView tvSalesPersonContact;

  @NonNull
  public final AppCompatTextView tvSalesPersonName;

  private ActivityCallUsBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull LinearLayoutCompat llMainLayout, @NonNull ProgressBarBinding llProgressLayout,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull AppCompatTextView tvAccPersonContact,
      @NonNull AppCompatTextView tvAccPersonName, @NonNull TextView tvCallAccount,
      @NonNull TextView tvCallCust, @NonNull TextView tvCallSales,
      @NonNull AppCompatTextView tvCustContact, @NonNull AppCompatTextView tvCustName,
      @NonNull AppCompatTextView tvSalesPersonContact,
      @NonNull AppCompatTextView tvSalesPersonName) {
    this.rootView = rootView;
    this.llMainLayout = llMainLayout;
    this.llProgressLayout = llProgressLayout;
    this.toolbar = toolbar;
    this.tvAccPersonContact = tvAccPersonContact;
    this.tvAccPersonName = tvAccPersonName;
    this.tvCallAccount = tvCallAccount;
    this.tvCallCust = tvCallCust;
    this.tvCallSales = tvCallSales;
    this.tvCustContact = tvCustContact;
    this.tvCustName = tvCustName;
    this.tvSalesPersonContact = tvSalesPersonContact;
    this.tvSalesPersonName = tvSalesPersonName;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCallUsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCallUsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_call_us, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCallUsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.llMainLayout;
      LinearLayoutCompat llMainLayout = ViewBindings.findChildViewById(rootView, id);
      if (llMainLayout == null) {
        break missingId;
      }

      id = R.id.llProgressLayout;
      View llProgressLayout = ViewBindings.findChildViewById(rootView, id);
      if (llProgressLayout == null) {
        break missingId;
      }
      ProgressBarBinding binding_llProgressLayout = ProgressBarBinding.bind(llProgressLayout);

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvAccPersonContact;
      AppCompatTextView tvAccPersonContact = ViewBindings.findChildViewById(rootView, id);
      if (tvAccPersonContact == null) {
        break missingId;
      }

      id = R.id.tvAccPersonName;
      AppCompatTextView tvAccPersonName = ViewBindings.findChildViewById(rootView, id);
      if (tvAccPersonName == null) {
        break missingId;
      }

      id = R.id.tvCallAccount;
      TextView tvCallAccount = ViewBindings.findChildViewById(rootView, id);
      if (tvCallAccount == null) {
        break missingId;
      }

      id = R.id.tvCallCust;
      TextView tvCallCust = ViewBindings.findChildViewById(rootView, id);
      if (tvCallCust == null) {
        break missingId;
      }

      id = R.id.tvCallSales;
      TextView tvCallSales = ViewBindings.findChildViewById(rootView, id);
      if (tvCallSales == null) {
        break missingId;
      }

      id = R.id.tvCustContact;
      AppCompatTextView tvCustContact = ViewBindings.findChildViewById(rootView, id);
      if (tvCustContact == null) {
        break missingId;
      }

      id = R.id.tvCustName;
      AppCompatTextView tvCustName = ViewBindings.findChildViewById(rootView, id);
      if (tvCustName == null) {
        break missingId;
      }

      id = R.id.tvSalesPersonContact;
      AppCompatTextView tvSalesPersonContact = ViewBindings.findChildViewById(rootView, id);
      if (tvSalesPersonContact == null) {
        break missingId;
      }

      id = R.id.tvSalesPersonName;
      AppCompatTextView tvSalesPersonName = ViewBindings.findChildViewById(rootView, id);
      if (tvSalesPersonName == null) {
        break missingId;
      }

      return new ActivityCallUsBinding((LinearLayoutCompat) rootView, llMainLayout,
          binding_llProgressLayout, binding_toolbar, tvAccPersonContact, tvAccPersonName,
          tvCallAccount, tvCallCust, tvCallSales, tvCustContact, tvCustName, tvSalesPersonContact,
          tvSalesPersonName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
