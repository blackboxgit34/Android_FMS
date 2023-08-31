// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ToolbarLayoutBinding implements ViewBinding {
  @NonNull
  private final Toolbar rootView;

  @NonNull
  public final TextView badge;

  @NonNull
  public final AppCompatImageView ivAdd;

  @NonNull
  public final AppCompatImageView ivBack;

  @NonNull
  public final AppCompatImageView ivBell;

  @NonNull
  public final ImageView ivLogo;

  @NonNull
  public final AppCompatImageView ivMarkIcon;

  @NonNull
  public final AppCompatImageView ivMenu;

  @NonNull
  public final AppCompatImageView ivSort;

  @NonNull
  public final TextView markCount;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final AppCompatTextView tvMarkAll;

  @NonNull
  public final AppCompatTextView tvTitle;

  private ToolbarLayoutBinding(@NonNull Toolbar rootView, @NonNull TextView badge,
      @NonNull AppCompatImageView ivAdd, @NonNull AppCompatImageView ivBack,
      @NonNull AppCompatImageView ivBell, @NonNull ImageView ivLogo,
      @NonNull AppCompatImageView ivMarkIcon, @NonNull AppCompatImageView ivMenu,
      @NonNull AppCompatImageView ivSort, @NonNull TextView markCount, @NonNull Toolbar toolbar,
      @NonNull AppCompatTextView tvMarkAll, @NonNull AppCompatTextView tvTitle) {
    this.rootView = rootView;
    this.badge = badge;
    this.ivAdd = ivAdd;
    this.ivBack = ivBack;
    this.ivBell = ivBell;
    this.ivLogo = ivLogo;
    this.ivMarkIcon = ivMarkIcon;
    this.ivMenu = ivMenu;
    this.ivSort = ivSort;
    this.markCount = markCount;
    this.toolbar = toolbar;
    this.tvMarkAll = tvMarkAll;
    this.tvTitle = tvTitle;
  }

  @Override
  @NonNull
  public Toolbar getRoot() {
    return rootView;
  }

  @NonNull
  public static ToolbarLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ToolbarLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.toolbar_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ToolbarLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.badge;
      TextView badge = ViewBindings.findChildViewById(rootView, id);
      if (badge == null) {
        break missingId;
      }

      id = R.id.ivAdd;
      AppCompatImageView ivAdd = ViewBindings.findChildViewById(rootView, id);
      if (ivAdd == null) {
        break missingId;
      }

      id = R.id.ivBack;
      AppCompatImageView ivBack = ViewBindings.findChildViewById(rootView, id);
      if (ivBack == null) {
        break missingId;
      }

      id = R.id.ivBell;
      AppCompatImageView ivBell = ViewBindings.findChildViewById(rootView, id);
      if (ivBell == null) {
        break missingId;
      }

      id = R.id.ivLogo;
      ImageView ivLogo = ViewBindings.findChildViewById(rootView, id);
      if (ivLogo == null) {
        break missingId;
      }

      id = R.id.ivMarkIcon;
      AppCompatImageView ivMarkIcon = ViewBindings.findChildViewById(rootView, id);
      if (ivMarkIcon == null) {
        break missingId;
      }

      id = R.id.ivMenu;
      AppCompatImageView ivMenu = ViewBindings.findChildViewById(rootView, id);
      if (ivMenu == null) {
        break missingId;
      }

      id = R.id.ivSort;
      AppCompatImageView ivSort = ViewBindings.findChildViewById(rootView, id);
      if (ivSort == null) {
        break missingId;
      }

      id = R.id.markCount;
      TextView markCount = ViewBindings.findChildViewById(rootView, id);
      if (markCount == null) {
        break missingId;
      }

      Toolbar toolbar = (Toolbar) rootView;

      id = R.id.tvMarkAll;
      AppCompatTextView tvMarkAll = ViewBindings.findChildViewById(rootView, id);
      if (tvMarkAll == null) {
        break missingId;
      }

      id = R.id.tvTitle;
      AppCompatTextView tvTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvTitle == null) {
        break missingId;
      }

      return new ToolbarLayoutBinding((Toolbar) rootView, badge, ivAdd, ivBack, ivBell, ivLogo,
          ivMarkIcon, ivMenu, ivSort, markCount, toolbar, tvMarkAll, tvTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
