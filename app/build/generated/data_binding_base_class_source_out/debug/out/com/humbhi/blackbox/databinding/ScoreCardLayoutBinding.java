// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
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

public final class ScoreCardLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final CardView mainLayout;

  @NonNull
  public final RatingBar ratingBar;

  @NonNull
  public final TextView tvAS;

  @NonNull
  public final TextView tvDriverName;

  @NonNull
  public final TextView tvRamarks;

  @NonNull
  public final TextView tvSNo;

  private ScoreCardLayoutBinding(@NonNull LinearLayoutCompat rootView, @NonNull CardView mainLayout,
      @NonNull RatingBar ratingBar, @NonNull TextView tvAS, @NonNull TextView tvDriverName,
      @NonNull TextView tvRamarks, @NonNull TextView tvSNo) {
    this.rootView = rootView;
    this.mainLayout = mainLayout;
    this.ratingBar = ratingBar;
    this.tvAS = tvAS;
    this.tvDriverName = tvDriverName;
    this.tvRamarks = tvRamarks;
    this.tvSNo = tvSNo;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ScoreCardLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ScoreCardLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.score_card_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ScoreCardLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.main_layout;
      CardView mainLayout = ViewBindings.findChildViewById(rootView, id);
      if (mainLayout == null) {
        break missingId;
      }

      id = R.id.ratingBar;
      RatingBar ratingBar = ViewBindings.findChildViewById(rootView, id);
      if (ratingBar == null) {
        break missingId;
      }

      id = R.id.tvAS;
      TextView tvAS = ViewBindings.findChildViewById(rootView, id);
      if (tvAS == null) {
        break missingId;
      }

      id = R.id.tvDriverName;
      TextView tvDriverName = ViewBindings.findChildViewById(rootView, id);
      if (tvDriverName == null) {
        break missingId;
      }

      id = R.id.tvRamarks;
      TextView tvRamarks = ViewBindings.findChildViewById(rootView, id);
      if (tvRamarks == null) {
        break missingId;
      }

      id = R.id.tvSNo;
      TextView tvSNo = ViewBindings.findChildViewById(rootView, id);
      if (tvSNo == null) {
        break missingId;
      }

      return new ScoreCardLayoutBinding((LinearLayoutCompat) rootView, mainLayout, ratingBar, tvAS,
          tvDriverName, tvRamarks, tvSNo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
