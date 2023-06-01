// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDashboardBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final BarChart DistanceChart;

  @NonNull
  public final LinearLayoutCompat clDriver;

  @NonNull
  public final ConstraintLayout clFleet;

  @NonNull
  public final ConstraintLayout clFuel;

  @NonNull
  public final ConstraintLayout clTotalCountVehicle;

  @NonNull
  public final ConstraintLayout clTotalReminders;

  @NonNull
  public final ConstraintLayout clTotalVehicle;

  @NonNull
  public final CardView cvDriverAnalysis;

  @NonNull
  public final CardView cvFleet;

  @NonNull
  public final CardView cvFuelAnalysis;

  @NonNull
  public final CardView cvReminders;

  @NonNull
  public final CardView cvTotalVehiclesDetail;

  @NonNull
  public final PieChart fleetChart;

  @NonNull
  public final PieChart fuelChart;

  @NonNull
  public final AppCompatImageView ivTotalReminderIcon;

  @NonNull
  public final AppCompatImageView ivTotalVehicleIcon;

  @NonNull
  public final LinearLayoutCompat llAllVehicle;

  @NonNull
  public final LinearLayout llAvgSpeed;

  @NonNull
  public final LinearLayout llAvgSpeedProgress;

  @NonNull
  public final LinearLayout llBars;

  @NonNull
  public final LinearLayoutCompat llBatteryDisconnected;

  @NonNull
  public final LinearLayout llDrivingBehavProgress;

  @NonNull
  public final LinearLayoutCompat llDrivingBehaviour;

  @NonNull
  public final LinearLayoutCompat llDrivingData;

  @NonNull
  public final LinearLayout llFleetProgress;

  @NonNull
  public final LinearLayout llFuelProgress;

  @NonNull
  public final LinearLayoutCompat llHighspeed;

  @NonNull
  public final LinearLayout llIdealPercentage;

  @NonNull
  public final LinearLayoutCompat llIgnitionOn;

  @NonNull
  public final LinearLayoutCompat llMainLayout;

  @NonNull
  public final LinearLayoutCompat llMoving;

  @NonNull
  public final LinearLayout llMovingPercentage;

  @NonNull
  public final LinearLayout llOverspeed;

  @NonNull
  public final LinearLayout llOverspeedProgress;

  @NonNull
  public final LinearLayoutCompat llParked;

  @NonNull
  public final LinearLayout llParkingPercentage;

  @NonNull
  public final LinearLayout llSpeedAnalysis;

  @NonNull
  public final LinearLayoutCompat llTotalVehicleMain;

  @NonNull
  public final LinearLayout llTotalVhclProgress;

  @NonNull
  public final LinearLayoutCompat llUnreachable;

  @NonNull
  public final TextView marqueeText;

  @NonNull
  public final TextView noFuelAvailable;

  @NonNull
  public final ProgressBarBinding progress;

  @NonNull
  public final RelativeLayout relativeLayout;

  @NonNull
  public final RelativeLayout rlDriverBehavChart;

  @NonNull
  public final RelativeLayout rlDrivingNotAvailable;

  @NonNull
  public final RelativeLayout rlFuelNotAvailable;

  @NonNull
  public final AppCompatTextView tvAboveAvg;

  @NonNull
  public final AppCompatTextView tvAboveAvgLabel;

  @NonNull
  public final AppCompatTextView tvAvgSpeed;

  @NonNull
  public final AppCompatTextView tvBatDisconnectionVehicle;

  @NonNull
  public final AppCompatTextView tvBatteryDisconneted;

  @NonNull
  public final AppCompatTextView tvDriving;

  @NonNull
  public final AppCompatTextView tvFleetUnitilization;

  @NonNull
  public final AppCompatTextView tvFuelAnalysis;

  @NonNull
  public final TextView tvHarshAccCount;

  @NonNull
  public final TextView tvHarshBreakCount;

  @NonNull
  public final TextView tvHarshOverspeedCount;

  @NonNull
  public final AppCompatTextView tvHighestAvg;

  @NonNull
  public final AppCompatTextView tvHighestLabel;

  @NonNull
  public final AppCompatTextView tvHighspeed;

  @NonNull
  public final AppCompatTextView tvIdeal;

  @NonNull
  public final AppCompatTextView tvIdealPercentage;

  @NonNull
  public final AppCompatTextView tvMoving;

  @NonNull
  public final AppCompatTextView tvMovingPercentage;

  @NonNull
  public final AppCompatTextView tvMovingVehicle;

  @NonNull
  public final AppCompatTextView tvOverspeed;

  @NonNull
  public final AppCompatTextView tvParked;

  @NonNull
  public final AppCompatTextView tvParkedPercentage;

  @NonNull
  public final AppCompatTextView tvParkedVehicle;

  @NonNull
  public final TextView tvRashTurnCount;

  @NonNull
  public final AppCompatTextView tvReminderCount;

  @NonNull
  public final AppCompatTextView tvReminderHeading;

  @NonNull
  public final TextView tvTotal;

  @NonNull
  public final AppCompatTextView tvTotalVehicle;

  @NonNull
  public final AppCompatTextView tvTotalVehicleHeading;

  @NonNull
  public final AppCompatTextView tvUnreachVehicle;

  private FragmentDashboardBinding(@NonNull FrameLayout rootView, @NonNull BarChart DistanceChart,
      @NonNull LinearLayoutCompat clDriver, @NonNull ConstraintLayout clFleet,
      @NonNull ConstraintLayout clFuel, @NonNull ConstraintLayout clTotalCountVehicle,
      @NonNull ConstraintLayout clTotalReminders, @NonNull ConstraintLayout clTotalVehicle,
      @NonNull CardView cvDriverAnalysis, @NonNull CardView cvFleet,
      @NonNull CardView cvFuelAnalysis, @NonNull CardView cvReminders,
      @NonNull CardView cvTotalVehiclesDetail, @NonNull PieChart fleetChart,
      @NonNull PieChart fuelChart, @NonNull AppCompatImageView ivTotalReminderIcon,
      @NonNull AppCompatImageView ivTotalVehicleIcon, @NonNull LinearLayoutCompat llAllVehicle,
      @NonNull LinearLayout llAvgSpeed, @NonNull LinearLayout llAvgSpeedProgress,
      @NonNull LinearLayout llBars, @NonNull LinearLayoutCompat llBatteryDisconnected,
      @NonNull LinearLayout llDrivingBehavProgress, @NonNull LinearLayoutCompat llDrivingBehaviour,
      @NonNull LinearLayoutCompat llDrivingData, @NonNull LinearLayout llFleetProgress,
      @NonNull LinearLayout llFuelProgress, @NonNull LinearLayoutCompat llHighspeed,
      @NonNull LinearLayout llIdealPercentage, @NonNull LinearLayoutCompat llIgnitionOn,
      @NonNull LinearLayoutCompat llMainLayout, @NonNull LinearLayoutCompat llMoving,
      @NonNull LinearLayout llMovingPercentage, @NonNull LinearLayout llOverspeed,
      @NonNull LinearLayout llOverspeedProgress, @NonNull LinearLayoutCompat llParked,
      @NonNull LinearLayout llParkingPercentage, @NonNull LinearLayout llSpeedAnalysis,
      @NonNull LinearLayoutCompat llTotalVehicleMain, @NonNull LinearLayout llTotalVhclProgress,
      @NonNull LinearLayoutCompat llUnreachable, @NonNull TextView marqueeText,
      @NonNull TextView noFuelAvailable, @NonNull ProgressBarBinding progress,
      @NonNull RelativeLayout relativeLayout, @NonNull RelativeLayout rlDriverBehavChart,
      @NonNull RelativeLayout rlDrivingNotAvailable, @NonNull RelativeLayout rlFuelNotAvailable,
      @NonNull AppCompatTextView tvAboveAvg, @NonNull AppCompatTextView tvAboveAvgLabel,
      @NonNull AppCompatTextView tvAvgSpeed, @NonNull AppCompatTextView tvBatDisconnectionVehicle,
      @NonNull AppCompatTextView tvBatteryDisconneted, @NonNull AppCompatTextView tvDriving,
      @NonNull AppCompatTextView tvFleetUnitilization, @NonNull AppCompatTextView tvFuelAnalysis,
      @NonNull TextView tvHarshAccCount, @NonNull TextView tvHarshBreakCount,
      @NonNull TextView tvHarshOverspeedCount, @NonNull AppCompatTextView tvHighestAvg,
      @NonNull AppCompatTextView tvHighestLabel, @NonNull AppCompatTextView tvHighspeed,
      @NonNull AppCompatTextView tvIdeal, @NonNull AppCompatTextView tvIdealPercentage,
      @NonNull AppCompatTextView tvMoving, @NonNull AppCompatTextView tvMovingPercentage,
      @NonNull AppCompatTextView tvMovingVehicle, @NonNull AppCompatTextView tvOverspeed,
      @NonNull AppCompatTextView tvParked, @NonNull AppCompatTextView tvParkedPercentage,
      @NonNull AppCompatTextView tvParkedVehicle, @NonNull TextView tvRashTurnCount,
      @NonNull AppCompatTextView tvReminderCount, @NonNull AppCompatTextView tvReminderHeading,
      @NonNull TextView tvTotal, @NonNull AppCompatTextView tvTotalVehicle,
      @NonNull AppCompatTextView tvTotalVehicleHeading,
      @NonNull AppCompatTextView tvUnreachVehicle) {
    this.rootView = rootView;
    this.DistanceChart = DistanceChart;
    this.clDriver = clDriver;
    this.clFleet = clFleet;
    this.clFuel = clFuel;
    this.clTotalCountVehicle = clTotalCountVehicle;
    this.clTotalReminders = clTotalReminders;
    this.clTotalVehicle = clTotalVehicle;
    this.cvDriverAnalysis = cvDriverAnalysis;
    this.cvFleet = cvFleet;
    this.cvFuelAnalysis = cvFuelAnalysis;
    this.cvReminders = cvReminders;
    this.cvTotalVehiclesDetail = cvTotalVehiclesDetail;
    this.fleetChart = fleetChart;
    this.fuelChart = fuelChart;
    this.ivTotalReminderIcon = ivTotalReminderIcon;
    this.ivTotalVehicleIcon = ivTotalVehicleIcon;
    this.llAllVehicle = llAllVehicle;
    this.llAvgSpeed = llAvgSpeed;
    this.llAvgSpeedProgress = llAvgSpeedProgress;
    this.llBars = llBars;
    this.llBatteryDisconnected = llBatteryDisconnected;
    this.llDrivingBehavProgress = llDrivingBehavProgress;
    this.llDrivingBehaviour = llDrivingBehaviour;
    this.llDrivingData = llDrivingData;
    this.llFleetProgress = llFleetProgress;
    this.llFuelProgress = llFuelProgress;
    this.llHighspeed = llHighspeed;
    this.llIdealPercentage = llIdealPercentage;
    this.llIgnitionOn = llIgnitionOn;
    this.llMainLayout = llMainLayout;
    this.llMoving = llMoving;
    this.llMovingPercentage = llMovingPercentage;
    this.llOverspeed = llOverspeed;
    this.llOverspeedProgress = llOverspeedProgress;
    this.llParked = llParked;
    this.llParkingPercentage = llParkingPercentage;
    this.llSpeedAnalysis = llSpeedAnalysis;
    this.llTotalVehicleMain = llTotalVehicleMain;
    this.llTotalVhclProgress = llTotalVhclProgress;
    this.llUnreachable = llUnreachable;
    this.marqueeText = marqueeText;
    this.noFuelAvailable = noFuelAvailable;
    this.progress = progress;
    this.relativeLayout = relativeLayout;
    this.rlDriverBehavChart = rlDriverBehavChart;
    this.rlDrivingNotAvailable = rlDrivingNotAvailable;
    this.rlFuelNotAvailable = rlFuelNotAvailable;
    this.tvAboveAvg = tvAboveAvg;
    this.tvAboveAvgLabel = tvAboveAvgLabel;
    this.tvAvgSpeed = tvAvgSpeed;
    this.tvBatDisconnectionVehicle = tvBatDisconnectionVehicle;
    this.tvBatteryDisconneted = tvBatteryDisconneted;
    this.tvDriving = tvDriving;
    this.tvFleetUnitilization = tvFleetUnitilization;
    this.tvFuelAnalysis = tvFuelAnalysis;
    this.tvHarshAccCount = tvHarshAccCount;
    this.tvHarshBreakCount = tvHarshBreakCount;
    this.tvHarshOverspeedCount = tvHarshOverspeedCount;
    this.tvHighestAvg = tvHighestAvg;
    this.tvHighestLabel = tvHighestLabel;
    this.tvHighspeed = tvHighspeed;
    this.tvIdeal = tvIdeal;
    this.tvIdealPercentage = tvIdealPercentage;
    this.tvMoving = tvMoving;
    this.tvMovingPercentage = tvMovingPercentage;
    this.tvMovingVehicle = tvMovingVehicle;
    this.tvOverspeed = tvOverspeed;
    this.tvParked = tvParked;
    this.tvParkedPercentage = tvParkedPercentage;
    this.tvParkedVehicle = tvParkedVehicle;
    this.tvRashTurnCount = tvRashTurnCount;
    this.tvReminderCount = tvReminderCount;
    this.tvReminderHeading = tvReminderHeading;
    this.tvTotal = tvTotal;
    this.tvTotalVehicle = tvTotalVehicle;
    this.tvTotalVehicleHeading = tvTotalVehicleHeading;
    this.tvUnreachVehicle = tvUnreachVehicle;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.DistanceChart;
      BarChart DistanceChart = ViewBindings.findChildViewById(rootView, id);
      if (DistanceChart == null) {
        break missingId;
      }

      id = R.id.clDriver;
      LinearLayoutCompat clDriver = ViewBindings.findChildViewById(rootView, id);
      if (clDriver == null) {
        break missingId;
      }

      id = R.id.clFleet;
      ConstraintLayout clFleet = ViewBindings.findChildViewById(rootView, id);
      if (clFleet == null) {
        break missingId;
      }

      id = R.id.clFuel;
      ConstraintLayout clFuel = ViewBindings.findChildViewById(rootView, id);
      if (clFuel == null) {
        break missingId;
      }

      id = R.id.clTotalCountVehicle;
      ConstraintLayout clTotalCountVehicle = ViewBindings.findChildViewById(rootView, id);
      if (clTotalCountVehicle == null) {
        break missingId;
      }

      id = R.id.clTotalReminders;
      ConstraintLayout clTotalReminders = ViewBindings.findChildViewById(rootView, id);
      if (clTotalReminders == null) {
        break missingId;
      }

      id = R.id.clTotalVehicle;
      ConstraintLayout clTotalVehicle = ViewBindings.findChildViewById(rootView, id);
      if (clTotalVehicle == null) {
        break missingId;
      }

      id = R.id.cvDriverAnalysis;
      CardView cvDriverAnalysis = ViewBindings.findChildViewById(rootView, id);
      if (cvDriverAnalysis == null) {
        break missingId;
      }

      id = R.id.cvFleet;
      CardView cvFleet = ViewBindings.findChildViewById(rootView, id);
      if (cvFleet == null) {
        break missingId;
      }

      id = R.id.cvFuelAnalysis;
      CardView cvFuelAnalysis = ViewBindings.findChildViewById(rootView, id);
      if (cvFuelAnalysis == null) {
        break missingId;
      }

      id = R.id.cvReminders;
      CardView cvReminders = ViewBindings.findChildViewById(rootView, id);
      if (cvReminders == null) {
        break missingId;
      }

      id = R.id.cvTotalVehiclesDetail;
      CardView cvTotalVehiclesDetail = ViewBindings.findChildViewById(rootView, id);
      if (cvTotalVehiclesDetail == null) {
        break missingId;
      }

      id = R.id.fleetChart;
      PieChart fleetChart = ViewBindings.findChildViewById(rootView, id);
      if (fleetChart == null) {
        break missingId;
      }

      id = R.id.fuelChart;
      PieChart fuelChart = ViewBindings.findChildViewById(rootView, id);
      if (fuelChart == null) {
        break missingId;
      }

      id = R.id.ivTotalReminderIcon;
      AppCompatImageView ivTotalReminderIcon = ViewBindings.findChildViewById(rootView, id);
      if (ivTotalReminderIcon == null) {
        break missingId;
      }

      id = R.id.ivTotalVehicleIcon;
      AppCompatImageView ivTotalVehicleIcon = ViewBindings.findChildViewById(rootView, id);
      if (ivTotalVehicleIcon == null) {
        break missingId;
      }

      id = R.id.llAllVehicle;
      LinearLayoutCompat llAllVehicle = ViewBindings.findChildViewById(rootView, id);
      if (llAllVehicle == null) {
        break missingId;
      }

      id = R.id.llAvgSpeed;
      LinearLayout llAvgSpeed = ViewBindings.findChildViewById(rootView, id);
      if (llAvgSpeed == null) {
        break missingId;
      }

      id = R.id.llAvgSpeedProgress;
      LinearLayout llAvgSpeedProgress = ViewBindings.findChildViewById(rootView, id);
      if (llAvgSpeedProgress == null) {
        break missingId;
      }

      id = R.id.llBars;
      LinearLayout llBars = ViewBindings.findChildViewById(rootView, id);
      if (llBars == null) {
        break missingId;
      }

      id = R.id.llBatteryDisconnected;
      LinearLayoutCompat llBatteryDisconnected = ViewBindings.findChildViewById(rootView, id);
      if (llBatteryDisconnected == null) {
        break missingId;
      }

      id = R.id.llDrivingBehavProgress;
      LinearLayout llDrivingBehavProgress = ViewBindings.findChildViewById(rootView, id);
      if (llDrivingBehavProgress == null) {
        break missingId;
      }

      id = R.id.llDrivingBehaviour;
      LinearLayoutCompat llDrivingBehaviour = ViewBindings.findChildViewById(rootView, id);
      if (llDrivingBehaviour == null) {
        break missingId;
      }

      id = R.id.llDrivingData;
      LinearLayoutCompat llDrivingData = ViewBindings.findChildViewById(rootView, id);
      if (llDrivingData == null) {
        break missingId;
      }

      id = R.id.llFleetProgress;
      LinearLayout llFleetProgress = ViewBindings.findChildViewById(rootView, id);
      if (llFleetProgress == null) {
        break missingId;
      }

      id = R.id.llFuelProgress;
      LinearLayout llFuelProgress = ViewBindings.findChildViewById(rootView, id);
      if (llFuelProgress == null) {
        break missingId;
      }

      id = R.id.llHighspeed;
      LinearLayoutCompat llHighspeed = ViewBindings.findChildViewById(rootView, id);
      if (llHighspeed == null) {
        break missingId;
      }

      id = R.id.llIdealPercentage;
      LinearLayout llIdealPercentage = ViewBindings.findChildViewById(rootView, id);
      if (llIdealPercentage == null) {
        break missingId;
      }

      id = R.id.llIgnitionOn;
      LinearLayoutCompat llIgnitionOn = ViewBindings.findChildViewById(rootView, id);
      if (llIgnitionOn == null) {
        break missingId;
      }

      id = R.id.llMainLayout;
      LinearLayoutCompat llMainLayout = ViewBindings.findChildViewById(rootView, id);
      if (llMainLayout == null) {
        break missingId;
      }

      id = R.id.llMoving;
      LinearLayoutCompat llMoving = ViewBindings.findChildViewById(rootView, id);
      if (llMoving == null) {
        break missingId;
      }

      id = R.id.llMovingPercentage;
      LinearLayout llMovingPercentage = ViewBindings.findChildViewById(rootView, id);
      if (llMovingPercentage == null) {
        break missingId;
      }

      id = R.id.llOverspeed;
      LinearLayout llOverspeed = ViewBindings.findChildViewById(rootView, id);
      if (llOverspeed == null) {
        break missingId;
      }

      id = R.id.llOverspeedProgress;
      LinearLayout llOverspeedProgress = ViewBindings.findChildViewById(rootView, id);
      if (llOverspeedProgress == null) {
        break missingId;
      }

      id = R.id.llParked;
      LinearLayoutCompat llParked = ViewBindings.findChildViewById(rootView, id);
      if (llParked == null) {
        break missingId;
      }

      id = R.id.llParkingPercentage;
      LinearLayout llParkingPercentage = ViewBindings.findChildViewById(rootView, id);
      if (llParkingPercentage == null) {
        break missingId;
      }

      id = R.id.llSpeedAnalysis;
      LinearLayout llSpeedAnalysis = ViewBindings.findChildViewById(rootView, id);
      if (llSpeedAnalysis == null) {
        break missingId;
      }

      id = R.id.llTotalVehicleMain;
      LinearLayoutCompat llTotalVehicleMain = ViewBindings.findChildViewById(rootView, id);
      if (llTotalVehicleMain == null) {
        break missingId;
      }

      id = R.id.llTotalVhclProgress;
      LinearLayout llTotalVhclProgress = ViewBindings.findChildViewById(rootView, id);
      if (llTotalVhclProgress == null) {
        break missingId;
      }

      id = R.id.llUnreachable;
      LinearLayoutCompat llUnreachable = ViewBindings.findChildViewById(rootView, id);
      if (llUnreachable == null) {
        break missingId;
      }

      id = R.id.marqueeText;
      TextView marqueeText = ViewBindings.findChildViewById(rootView, id);
      if (marqueeText == null) {
        break missingId;
      }

      id = R.id.noFuelAvailable;
      TextView noFuelAvailable = ViewBindings.findChildViewById(rootView, id);
      if (noFuelAvailable == null) {
        break missingId;
      }

      id = R.id.progress;
      View progress = ViewBindings.findChildViewById(rootView, id);
      if (progress == null) {
        break missingId;
      }
      ProgressBarBinding binding_progress = ProgressBarBinding.bind(progress);

      id = R.id.relativeLayout;
      RelativeLayout relativeLayout = ViewBindings.findChildViewById(rootView, id);
      if (relativeLayout == null) {
        break missingId;
      }

      id = R.id.rlDriverBehavChart;
      RelativeLayout rlDriverBehavChart = ViewBindings.findChildViewById(rootView, id);
      if (rlDriverBehavChart == null) {
        break missingId;
      }

      id = R.id.rlDrivingNotAvailable;
      RelativeLayout rlDrivingNotAvailable = ViewBindings.findChildViewById(rootView, id);
      if (rlDrivingNotAvailable == null) {
        break missingId;
      }

      id = R.id.rlFuelNotAvailable;
      RelativeLayout rlFuelNotAvailable = ViewBindings.findChildViewById(rootView, id);
      if (rlFuelNotAvailable == null) {
        break missingId;
      }

      id = R.id.tvAboveAvg;
      AppCompatTextView tvAboveAvg = ViewBindings.findChildViewById(rootView, id);
      if (tvAboveAvg == null) {
        break missingId;
      }

      id = R.id.tvAboveAvgLabel;
      AppCompatTextView tvAboveAvgLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvAboveAvgLabel == null) {
        break missingId;
      }

      id = R.id.tvAvgSpeed;
      AppCompatTextView tvAvgSpeed = ViewBindings.findChildViewById(rootView, id);
      if (tvAvgSpeed == null) {
        break missingId;
      }

      id = R.id.tvBatDisconnectionVehicle;
      AppCompatTextView tvBatDisconnectionVehicle = ViewBindings.findChildViewById(rootView, id);
      if (tvBatDisconnectionVehicle == null) {
        break missingId;
      }

      id = R.id.tvBatteryDisconneted;
      AppCompatTextView tvBatteryDisconneted = ViewBindings.findChildViewById(rootView, id);
      if (tvBatteryDisconneted == null) {
        break missingId;
      }

      id = R.id.tvDriving;
      AppCompatTextView tvDriving = ViewBindings.findChildViewById(rootView, id);
      if (tvDriving == null) {
        break missingId;
      }

      id = R.id.tvFleetUnitilization;
      AppCompatTextView tvFleetUnitilization = ViewBindings.findChildViewById(rootView, id);
      if (tvFleetUnitilization == null) {
        break missingId;
      }

      id = R.id.tvFuelAnalysis;
      AppCompatTextView tvFuelAnalysis = ViewBindings.findChildViewById(rootView, id);
      if (tvFuelAnalysis == null) {
        break missingId;
      }

      id = R.id.tvHarshAccCount;
      TextView tvHarshAccCount = ViewBindings.findChildViewById(rootView, id);
      if (tvHarshAccCount == null) {
        break missingId;
      }

      id = R.id.tvHarshBreakCount;
      TextView tvHarshBreakCount = ViewBindings.findChildViewById(rootView, id);
      if (tvHarshBreakCount == null) {
        break missingId;
      }

      id = R.id.tvHarshOverspeedCount;
      TextView tvHarshOverspeedCount = ViewBindings.findChildViewById(rootView, id);
      if (tvHarshOverspeedCount == null) {
        break missingId;
      }

      id = R.id.tvHighestAvg;
      AppCompatTextView tvHighestAvg = ViewBindings.findChildViewById(rootView, id);
      if (tvHighestAvg == null) {
        break missingId;
      }

      id = R.id.tvHighestLabel;
      AppCompatTextView tvHighestLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvHighestLabel == null) {
        break missingId;
      }

      id = R.id.tvHighspeed;
      AppCompatTextView tvHighspeed = ViewBindings.findChildViewById(rootView, id);
      if (tvHighspeed == null) {
        break missingId;
      }

      id = R.id.tvIdeal;
      AppCompatTextView tvIdeal = ViewBindings.findChildViewById(rootView, id);
      if (tvIdeal == null) {
        break missingId;
      }

      id = R.id.tvIdealPercentage;
      AppCompatTextView tvIdealPercentage = ViewBindings.findChildViewById(rootView, id);
      if (tvIdealPercentage == null) {
        break missingId;
      }

      id = R.id.tvMoving;
      AppCompatTextView tvMoving = ViewBindings.findChildViewById(rootView, id);
      if (tvMoving == null) {
        break missingId;
      }

      id = R.id.tvMovingPercentage;
      AppCompatTextView tvMovingPercentage = ViewBindings.findChildViewById(rootView, id);
      if (tvMovingPercentage == null) {
        break missingId;
      }

      id = R.id.tvMovingVehicle;
      AppCompatTextView tvMovingVehicle = ViewBindings.findChildViewById(rootView, id);
      if (tvMovingVehicle == null) {
        break missingId;
      }

      id = R.id.tvOverspeed;
      AppCompatTextView tvOverspeed = ViewBindings.findChildViewById(rootView, id);
      if (tvOverspeed == null) {
        break missingId;
      }

      id = R.id.tvParked;
      AppCompatTextView tvParked = ViewBindings.findChildViewById(rootView, id);
      if (tvParked == null) {
        break missingId;
      }

      id = R.id.tvParkedPercentage;
      AppCompatTextView tvParkedPercentage = ViewBindings.findChildViewById(rootView, id);
      if (tvParkedPercentage == null) {
        break missingId;
      }

      id = R.id.tvParkedVehicle;
      AppCompatTextView tvParkedVehicle = ViewBindings.findChildViewById(rootView, id);
      if (tvParkedVehicle == null) {
        break missingId;
      }

      id = R.id.tvRashTurnCount;
      TextView tvRashTurnCount = ViewBindings.findChildViewById(rootView, id);
      if (tvRashTurnCount == null) {
        break missingId;
      }

      id = R.id.tvReminderCount;
      AppCompatTextView tvReminderCount = ViewBindings.findChildViewById(rootView, id);
      if (tvReminderCount == null) {
        break missingId;
      }

      id = R.id.tvReminderHeading;
      AppCompatTextView tvReminderHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvReminderHeading == null) {
        break missingId;
      }

      id = R.id.tvTotal;
      TextView tvTotal = ViewBindings.findChildViewById(rootView, id);
      if (tvTotal == null) {
        break missingId;
      }

      id = R.id.tvTotalVehicle;
      AppCompatTextView tvTotalVehicle = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalVehicle == null) {
        break missingId;
      }

      id = R.id.tvTotalVehicleHeading;
      AppCompatTextView tvTotalVehicleHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalVehicleHeading == null) {
        break missingId;
      }

      id = R.id.tvUnreachVehicle;
      AppCompatTextView tvUnreachVehicle = ViewBindings.findChildViewById(rootView, id);
      if (tvUnreachVehicle == null) {
        break missingId;
      }

      return new FragmentDashboardBinding((FrameLayout) rootView, DistanceChart, clDriver, clFleet,
          clFuel, clTotalCountVehicle, clTotalReminders, clTotalVehicle, cvDriverAnalysis, cvFleet,
          cvFuelAnalysis, cvReminders, cvTotalVehiclesDetail, fleetChart, fuelChart,
          ivTotalReminderIcon, ivTotalVehicleIcon, llAllVehicle, llAvgSpeed, llAvgSpeedProgress,
          llBars, llBatteryDisconnected, llDrivingBehavProgress, llDrivingBehaviour, llDrivingData,
          llFleetProgress, llFuelProgress, llHighspeed, llIdealPercentage, llIgnitionOn,
          llMainLayout, llMoving, llMovingPercentage, llOverspeed, llOverspeedProgress, llParked,
          llParkingPercentage, llSpeedAnalysis, llTotalVehicleMain, llTotalVhclProgress,
          llUnreachable, marqueeText, noFuelAvailable, binding_progress, relativeLayout,
          rlDriverBehavChart, rlDrivingNotAvailable, rlFuelNotAvailable, tvAboveAvg,
          tvAboveAvgLabel, tvAvgSpeed, tvBatDisconnectionVehicle, tvBatteryDisconneted, tvDriving,
          tvFleetUnitilization, tvFuelAnalysis, tvHarshAccCount, tvHarshBreakCount,
          tvHarshOverspeedCount, tvHighestAvg, tvHighestLabel, tvHighspeed, tvIdeal,
          tvIdealPercentage, tvMoving, tvMovingPercentage, tvMovingVehicle, tvOverspeed, tvParked,
          tvParkedPercentage, tvParkedVehicle, tvRashTurnCount, tvReminderCount, tvReminderHeading,
          tvTotal, tvTotalVehicle, tvTotalVehicleHeading, tvUnreachVehicle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
