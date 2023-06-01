// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRoutePlayBackBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView Drivingduration;

  @NonNull
  public final TextView Idlingduration;

  @NonNull
  public final RelativeLayout PlayAndPause;

  @NonNull
  public final TextView Stoppageduration;

  @NonNull
  public final CoordinatorLayout bottomSheetLayout;

  @NonNull
  public final CardView cvDistanceDetails;

  @NonNull
  public final LinearLayoutCompat durationLayout;

  @NonNull
  public final FrameLayout fBottomSheet;

  @NonNull
  public final LinearLayout llDate;

  @NonNull
  public final LinearLayout llDrivingVoilationIndicators;

  @NonNull
  public final LinearLayout llLocation;

  @NonNull
  public final LinearLayoutCompat llRoutePlaybackDetails;

  @NonNull
  public final FragmentContainerView map;

  @NonNull
  public final TextView mapView;

  @NonNull
  public final ImageView playPauseIcon;

  @NonNull
  public final AppCompatSeekBar sSeekBar;

  @NonNull
  public final PointerSpeedometer speedGauge;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final TextView tvCurrentDistanceCover;

  @NonNull
  public final TextView tvCurrentSpeed;

  @NonNull
  public final TextView tvDateAndTime;

  @NonNull
  public final AppCompatTextView tvDistance;

  @NonNull
  public final AppCompatTextView tvEndLocation;

  @NonNull
  public final TextView tvFourX;

  @NonNull
  public final TextView tvLocation;

  @NonNull
  public final TextView tvOneX;

  @NonNull
  public final AppCompatTextView tvStartocation;

  @NonNull
  public final TextView tvThreeX;

  @NonNull
  public final TextView tvTotalDistanceTravel;

  @NonNull
  public final TextView tvTwoX;

  private ActivityRoutePlayBackBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView Drivingduration, @NonNull TextView Idlingduration,
      @NonNull RelativeLayout PlayAndPause, @NonNull TextView Stoppageduration,
      @NonNull CoordinatorLayout bottomSheetLayout, @NonNull CardView cvDistanceDetails,
      @NonNull LinearLayoutCompat durationLayout, @NonNull FrameLayout fBottomSheet,
      @NonNull LinearLayout llDate, @NonNull LinearLayout llDrivingVoilationIndicators,
      @NonNull LinearLayout llLocation, @NonNull LinearLayoutCompat llRoutePlaybackDetails,
      @NonNull FragmentContainerView map, @NonNull TextView mapView,
      @NonNull ImageView playPauseIcon, @NonNull AppCompatSeekBar sSeekBar,
      @NonNull PointerSpeedometer speedGauge, @NonNull ToolbarLayoutBinding toolbar,
      @NonNull TextView tvCurrentDistanceCover, @NonNull TextView tvCurrentSpeed,
      @NonNull TextView tvDateAndTime, @NonNull AppCompatTextView tvDistance,
      @NonNull AppCompatTextView tvEndLocation, @NonNull TextView tvFourX,
      @NonNull TextView tvLocation, @NonNull TextView tvOneX,
      @NonNull AppCompatTextView tvStartocation, @NonNull TextView tvThreeX,
      @NonNull TextView tvTotalDistanceTravel, @NonNull TextView tvTwoX) {
    this.rootView = rootView;
    this.Drivingduration = Drivingduration;
    this.Idlingduration = Idlingduration;
    this.PlayAndPause = PlayAndPause;
    this.Stoppageduration = Stoppageduration;
    this.bottomSheetLayout = bottomSheetLayout;
    this.cvDistanceDetails = cvDistanceDetails;
    this.durationLayout = durationLayout;
    this.fBottomSheet = fBottomSheet;
    this.llDate = llDate;
    this.llDrivingVoilationIndicators = llDrivingVoilationIndicators;
    this.llLocation = llLocation;
    this.llRoutePlaybackDetails = llRoutePlaybackDetails;
    this.map = map;
    this.mapView = mapView;
    this.playPauseIcon = playPauseIcon;
    this.sSeekBar = sSeekBar;
    this.speedGauge = speedGauge;
    this.toolbar = toolbar;
    this.tvCurrentDistanceCover = tvCurrentDistanceCover;
    this.tvCurrentSpeed = tvCurrentSpeed;
    this.tvDateAndTime = tvDateAndTime;
    this.tvDistance = tvDistance;
    this.tvEndLocation = tvEndLocation;
    this.tvFourX = tvFourX;
    this.tvLocation = tvLocation;
    this.tvOneX = tvOneX;
    this.tvStartocation = tvStartocation;
    this.tvThreeX = tvThreeX;
    this.tvTotalDistanceTravel = tvTotalDistanceTravel;
    this.tvTwoX = tvTwoX;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRoutePlayBackBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRoutePlayBackBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_route_play_back, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRoutePlayBackBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Drivingduration;
      TextView Drivingduration = ViewBindings.findChildViewById(rootView, id);
      if (Drivingduration == null) {
        break missingId;
      }

      id = R.id.Idlingduration;
      TextView Idlingduration = ViewBindings.findChildViewById(rootView, id);
      if (Idlingduration == null) {
        break missingId;
      }

      id = R.id.PlayAndPause;
      RelativeLayout PlayAndPause = ViewBindings.findChildViewById(rootView, id);
      if (PlayAndPause == null) {
        break missingId;
      }

      id = R.id.Stoppageduration;
      TextView Stoppageduration = ViewBindings.findChildViewById(rootView, id);
      if (Stoppageduration == null) {
        break missingId;
      }

      id = R.id.bottomSheetLayout;
      CoordinatorLayout bottomSheetLayout = ViewBindings.findChildViewById(rootView, id);
      if (bottomSheetLayout == null) {
        break missingId;
      }

      id = R.id.cvDistanceDetails;
      CardView cvDistanceDetails = ViewBindings.findChildViewById(rootView, id);
      if (cvDistanceDetails == null) {
        break missingId;
      }

      id = R.id.durationLayout;
      LinearLayoutCompat durationLayout = ViewBindings.findChildViewById(rootView, id);
      if (durationLayout == null) {
        break missingId;
      }

      id = R.id.fBottomSheet;
      FrameLayout fBottomSheet = ViewBindings.findChildViewById(rootView, id);
      if (fBottomSheet == null) {
        break missingId;
      }

      id = R.id.llDate;
      LinearLayout llDate = ViewBindings.findChildViewById(rootView, id);
      if (llDate == null) {
        break missingId;
      }

      id = R.id.llDrivingVoilationIndicators;
      LinearLayout llDrivingVoilationIndicators = ViewBindings.findChildViewById(rootView, id);
      if (llDrivingVoilationIndicators == null) {
        break missingId;
      }

      id = R.id.llLocation;
      LinearLayout llLocation = ViewBindings.findChildViewById(rootView, id);
      if (llLocation == null) {
        break missingId;
      }

      id = R.id.llRoutePlaybackDetails;
      LinearLayoutCompat llRoutePlaybackDetails = ViewBindings.findChildViewById(rootView, id);
      if (llRoutePlaybackDetails == null) {
        break missingId;
      }

      id = R.id.map;
      FragmentContainerView map = ViewBindings.findChildViewById(rootView, id);
      if (map == null) {
        break missingId;
      }

      id = R.id.mapView;
      TextView mapView = ViewBindings.findChildViewById(rootView, id);
      if (mapView == null) {
        break missingId;
      }

      id = R.id.play_pause_icon;
      ImageView playPauseIcon = ViewBindings.findChildViewById(rootView, id);
      if (playPauseIcon == null) {
        break missingId;
      }

      id = R.id.sSeekBar;
      AppCompatSeekBar sSeekBar = ViewBindings.findChildViewById(rootView, id);
      if (sSeekBar == null) {
        break missingId;
      }

      id = R.id.speedGauge;
      PointerSpeedometer speedGauge = ViewBindings.findChildViewById(rootView, id);
      if (speedGauge == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvCurrentDistanceCover;
      TextView tvCurrentDistanceCover = ViewBindings.findChildViewById(rootView, id);
      if (tvCurrentDistanceCover == null) {
        break missingId;
      }

      id = R.id.tvCurrentSpeed;
      TextView tvCurrentSpeed = ViewBindings.findChildViewById(rootView, id);
      if (tvCurrentSpeed == null) {
        break missingId;
      }

      id = R.id.tvDateAndTime;
      TextView tvDateAndTime = ViewBindings.findChildViewById(rootView, id);
      if (tvDateAndTime == null) {
        break missingId;
      }

      id = R.id.tvDistance;
      AppCompatTextView tvDistance = ViewBindings.findChildViewById(rootView, id);
      if (tvDistance == null) {
        break missingId;
      }

      id = R.id.tvEndLocation;
      AppCompatTextView tvEndLocation = ViewBindings.findChildViewById(rootView, id);
      if (tvEndLocation == null) {
        break missingId;
      }

      id = R.id.tvFourX;
      TextView tvFourX = ViewBindings.findChildViewById(rootView, id);
      if (tvFourX == null) {
        break missingId;
      }

      id = R.id.tvLocation;
      TextView tvLocation = ViewBindings.findChildViewById(rootView, id);
      if (tvLocation == null) {
        break missingId;
      }

      id = R.id.tvOneX;
      TextView tvOneX = ViewBindings.findChildViewById(rootView, id);
      if (tvOneX == null) {
        break missingId;
      }

      id = R.id.tvStartocation;
      AppCompatTextView tvStartocation = ViewBindings.findChildViewById(rootView, id);
      if (tvStartocation == null) {
        break missingId;
      }

      id = R.id.tvThreeX;
      TextView tvThreeX = ViewBindings.findChildViewById(rootView, id);
      if (tvThreeX == null) {
        break missingId;
      }

      id = R.id.tvTotalDistanceTravel;
      TextView tvTotalDistanceTravel = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalDistanceTravel == null) {
        break missingId;
      }

      id = R.id.tvTwoX;
      TextView tvTwoX = ViewBindings.findChildViewById(rootView, id);
      if (tvTwoX == null) {
        break missingId;
      }

      return new ActivityRoutePlayBackBinding((ConstraintLayout) rootView, Drivingduration,
          Idlingduration, PlayAndPause, Stoppageduration, bottomSheetLayout, cvDistanceDetails,
          durationLayout, fBottomSheet, llDate, llDrivingVoilationIndicators, llLocation,
          llRoutePlaybackDetails, map, mapView, playPauseIcon, sSeekBar, speedGauge,
          binding_toolbar, tvCurrentDistanceCover, tvCurrentSpeed, tvDateAndTime, tvDistance,
          tvEndLocation, tvFourX, tvLocation, tvOneX, tvStartocation, tvThreeX,
          tvTotalDistanceTravel, tvTwoX);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}