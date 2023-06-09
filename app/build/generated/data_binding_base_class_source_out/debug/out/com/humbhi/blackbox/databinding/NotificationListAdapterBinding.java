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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NotificationListAdapterBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatImageView ivCircleNotation;

  @NonNull
  public final ImageView ivDelete;

  @NonNull
  public final ImageView ivNotificationIcon;

  @NonNull
  public final ImageView ivShare;

  @NonNull
  public final LinearLayoutCompat llSelectNotification;

  @NonNull
  public final ImageView mark;

  @NonNull
  public final TextView tvDate;

  @NonNull
  public final TextView tvMessage;

  @NonNull
  public final TextView tvType;

  private NotificationListAdapterBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatImageView ivCircleNotation, @NonNull ImageView ivDelete,
      @NonNull ImageView ivNotificationIcon, @NonNull ImageView ivShare,
      @NonNull LinearLayoutCompat llSelectNotification, @NonNull ImageView mark,
      @NonNull TextView tvDate, @NonNull TextView tvMessage, @NonNull TextView tvType) {
    this.rootView = rootView;
    this.ivCircleNotation = ivCircleNotation;
    this.ivDelete = ivDelete;
    this.ivNotificationIcon = ivNotificationIcon;
    this.ivShare = ivShare;
    this.llSelectNotification = llSelectNotification;
    this.mark = mark;
    this.tvDate = tvDate;
    this.tvMessage = tvMessage;
    this.tvType = tvType;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NotificationListAdapterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NotificationListAdapterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.notification_list_adapter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NotificationListAdapterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ivCircleNotation;
      AppCompatImageView ivCircleNotation = ViewBindings.findChildViewById(rootView, id);
      if (ivCircleNotation == null) {
        break missingId;
      }

      id = R.id.ivDelete;
      ImageView ivDelete = ViewBindings.findChildViewById(rootView, id);
      if (ivDelete == null) {
        break missingId;
      }

      id = R.id.ivNotificationIcon;
      ImageView ivNotificationIcon = ViewBindings.findChildViewById(rootView, id);
      if (ivNotificationIcon == null) {
        break missingId;
      }

      id = R.id.ivShare;
      ImageView ivShare = ViewBindings.findChildViewById(rootView, id);
      if (ivShare == null) {
        break missingId;
      }

      id = R.id.llSelectNotification;
      LinearLayoutCompat llSelectNotification = ViewBindings.findChildViewById(rootView, id);
      if (llSelectNotification == null) {
        break missingId;
      }

      id = R.id.mark;
      ImageView mark = ViewBindings.findChildViewById(rootView, id);
      if (mark == null) {
        break missingId;
      }

      id = R.id.tvDate;
      TextView tvDate = ViewBindings.findChildViewById(rootView, id);
      if (tvDate == null) {
        break missingId;
      }

      id = R.id.tvMessage;
      TextView tvMessage = ViewBindings.findChildViewById(rootView, id);
      if (tvMessage == null) {
        break missingId;
      }

      id = R.id.tvType;
      TextView tvType = ViewBindings.findChildViewById(rootView, id);
      if (tvType == null) {
        break missingId;
      }

      return new NotificationListAdapterBinding((ConstraintLayout) rootView, ivCircleNotation,
          ivDelete, ivNotificationIcon, ivShare, llSelectNotification, mark, tvDate, tvMessage,
          tvType);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
