// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.mikephil.charting.charts.PieChart;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDrivingBehaviourBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final ConstraintLayout clTotalCountVehicle;

  @NonNull
  public final CardView cvConsolidateVoilationReport;

  @NonNull
  public final CardView cvDriverCompare;

  @NonNull
  public final CardView cvExcellent;

  @NonNull
  public final CardView cvHarshAccelerationReport;

  @NonNull
  public final CardView cvHarshBreakReport;

  @NonNull
  public final CardView cvRanking;

  @NonNull
  public final CardView cvRashTurnReport;

  @NonNull
  public final CardView cvReports;

  @NonNull
  public final CardView cvRiskyDriver;

  @NonNull
  public final CardView cvRoute;

  @NonNull
  public final CardView cvTotalVehiclesDetail;

  @NonNull
  public final PieChart driverClassificationChart;

  @NonNull
  public final LinearLayoutCompat drvrComparisonMonthly;

  @NonNull
  public final LinearLayoutCompat drvrToDrvrComparison;

  @NonNull
  public final PieChart fuelChart;

  @NonNull
  public final ImageView ivArrowExcellent;

  @NonNull
  public final ImageView ivArrowRisky;

  @NonNull
  public final AppCompatImageView ivTotalVehicleIcon;

  @NonNull
  public final LinearLayoutCompat llDrivingLimit;

  @NonNull
  public final LinearLayoutCompat llModerate;

  @NonNull
  public final LinearLayoutCompat llNoDrivingHours;

  @NonNull
  public final LinearLayoutCompat llRiskyDriverReport;

  @NonNull
  public final LinearLayoutCompat llSafeDriverReports;

  @NonNull
  public final LinearLayoutCompat llScore;

  @NonNull
  public final RelativeLayout relativeLayout;

  @NonNull
  public final RelativeLayout relativeLayoutDC;

  @NonNull
  public final RecyclerView rvExcellentDrivers;

  @NonNull
  public final RecyclerView rvRiskyDrivers;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  @NonNull
  public final AppCompatTextView tvDriverAnalysis;

  @NonNull
  public final AppCompatTextView tvDriverClassification;

  @NonNull
  public final AppCompatTextView tvDriverComparison;

  @NonNull
  public final AppCompatTextView tvDriverScore;

  @NonNull
  public final TextView tvExDriver;

  @NonNull
  public final AppCompatTextView tvFuelHeading;

  @NonNull
  public final AppCompatTextView tvFuelSubHeading;

  @NonNull
  public final TextView tvHarshAcc;

  @NonNull
  public final TextView tvHarshBreak;

  @NonNull
  public final TextView tvHarshOverspeed;

  @NonNull
  public final TextView tvModerateDriver;

  @NonNull
  public final AppCompatTextView tvRankingHeading;

  @NonNull
  public final TextView tvRashTurn;

  @NonNull
  public final AppCompatTextView tvReportsHeading;

  @NonNull
  public final AppCompatTextView tvRiskyDetails;

  @NonNull
  public final TextView tvRskyDriver;

  @NonNull
  public final AppCompatTextView tvSeeExcellentDriverDetails;

  @NonNull
  public final AppCompatTextView tvTotalVehicle;

  @NonNull
  public final AppCompatTextView tvTotalVehicleHeading;

  @NonNull
  public final TextView tvTotalVoilationExDriver;

  @NonNull
  public final TextView tvTotalVoilationRiskyDrivers;

  @NonNull
  public final View vViewExcellent;

  @NonNull
  public final View vViewRisky;

  private ActivityDrivingBehaviourBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull ConstraintLayout clTotalCountVehicle, @NonNull CardView cvConsolidateVoilationReport,
      @NonNull CardView cvDriverCompare, @NonNull CardView cvExcellent,
      @NonNull CardView cvHarshAccelerationReport, @NonNull CardView cvHarshBreakReport,
      @NonNull CardView cvRanking, @NonNull CardView cvRashTurnReport, @NonNull CardView cvReports,
      @NonNull CardView cvRiskyDriver, @NonNull CardView cvRoute,
      @NonNull CardView cvTotalVehiclesDetail, @NonNull PieChart driverClassificationChart,
      @NonNull LinearLayoutCompat drvrComparisonMonthly,
      @NonNull LinearLayoutCompat drvrToDrvrComparison, @NonNull PieChart fuelChart,
      @NonNull ImageView ivArrowExcellent, @NonNull ImageView ivArrowRisky,
      @NonNull AppCompatImageView ivTotalVehicleIcon, @NonNull LinearLayoutCompat llDrivingLimit,
      @NonNull LinearLayoutCompat llModerate, @NonNull LinearLayoutCompat llNoDrivingHours,
      @NonNull LinearLayoutCompat llRiskyDriverReport,
      @NonNull LinearLayoutCompat llSafeDriverReports, @NonNull LinearLayoutCompat llScore,
      @NonNull RelativeLayout relativeLayout, @NonNull RelativeLayout relativeLayoutDC,
      @NonNull RecyclerView rvExcellentDrivers, @NonNull RecyclerView rvRiskyDrivers,
      @NonNull ToolbarLayoutBinding toolbar, @NonNull AppCompatTextView tvDriverAnalysis,
      @NonNull AppCompatTextView tvDriverClassification,
      @NonNull AppCompatTextView tvDriverComparison, @NonNull AppCompatTextView tvDriverScore,
      @NonNull TextView tvExDriver, @NonNull AppCompatTextView tvFuelHeading,
      @NonNull AppCompatTextView tvFuelSubHeading, @NonNull TextView tvHarshAcc,
      @NonNull TextView tvHarshBreak, @NonNull TextView tvHarshOverspeed,
      @NonNull TextView tvModerateDriver, @NonNull AppCompatTextView tvRankingHeading,
      @NonNull TextView tvRashTurn, @NonNull AppCompatTextView tvReportsHeading,
      @NonNull AppCompatTextView tvRiskyDetails, @NonNull TextView tvRskyDriver,
      @NonNull AppCompatTextView tvSeeExcellentDriverDetails,
      @NonNull AppCompatTextView tvTotalVehicle, @NonNull AppCompatTextView tvTotalVehicleHeading,
      @NonNull TextView tvTotalVoilationExDriver, @NonNull TextView tvTotalVoilationRiskyDrivers,
      @NonNull View vViewExcellent, @NonNull View vViewRisky) {
    this.rootView = rootView;
    this.clTotalCountVehicle = clTotalCountVehicle;
    this.cvConsolidateVoilationReport = cvConsolidateVoilationReport;
    this.cvDriverCompare = cvDriverCompare;
    this.cvExcellent = cvExcellent;
    this.cvHarshAccelerationReport = cvHarshAccelerationReport;
    this.cvHarshBreakReport = cvHarshBreakReport;
    this.cvRanking = cvRanking;
    this.cvRashTurnReport = cvRashTurnReport;
    this.cvReports = cvReports;
    this.cvRiskyDriver = cvRiskyDriver;
    this.cvRoute = cvRoute;
    this.cvTotalVehiclesDetail = cvTotalVehiclesDetail;
    this.driverClassificationChart = driverClassificationChart;
    this.drvrComparisonMonthly = drvrComparisonMonthly;
    this.drvrToDrvrComparison = drvrToDrvrComparison;
    this.fuelChart = fuelChart;
    this.ivArrowExcellent = ivArrowExcellent;
    this.ivArrowRisky = ivArrowRisky;
    this.ivTotalVehicleIcon = ivTotalVehicleIcon;
    this.llDrivingLimit = llDrivingLimit;
    this.llModerate = llModerate;
    this.llNoDrivingHours = llNoDrivingHours;
    this.llRiskyDriverReport = llRiskyDriverReport;
    this.llSafeDriverReports = llSafeDriverReports;
    this.llScore = llScore;
    this.relativeLayout = relativeLayout;
    this.relativeLayoutDC = relativeLayoutDC;
    this.rvExcellentDrivers = rvExcellentDrivers;
    this.rvRiskyDrivers = rvRiskyDrivers;
    this.toolbar = toolbar;
    this.tvDriverAnalysis = tvDriverAnalysis;
    this.tvDriverClassification = tvDriverClassification;
    this.tvDriverComparison = tvDriverComparison;
    this.tvDriverScore = tvDriverScore;
    this.tvExDriver = tvExDriver;
    this.tvFuelHeading = tvFuelHeading;
    this.tvFuelSubHeading = tvFuelSubHeading;
    this.tvHarshAcc = tvHarshAcc;
    this.tvHarshBreak = tvHarshBreak;
    this.tvHarshOverspeed = tvHarshOverspeed;
    this.tvModerateDriver = tvModerateDriver;
    this.tvRankingHeading = tvRankingHeading;
    this.tvRashTurn = tvRashTurn;
    this.tvReportsHeading = tvReportsHeading;
    this.tvRiskyDetails = tvRiskyDetails;
    this.tvRskyDriver = tvRskyDriver;
    this.tvSeeExcellentDriverDetails = tvSeeExcellentDriverDetails;
    this.tvTotalVehicle = tvTotalVehicle;
    this.tvTotalVehicleHeading = tvTotalVehicleHeading;
    this.tvTotalVoilationExDriver = tvTotalVoilationExDriver;
    this.tvTotalVoilationRiskyDrivers = tvTotalVoilationRiskyDrivers;
    this.vViewExcellent = vViewExcellent;
    this.vViewRisky = vViewRisky;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDrivingBehaviourBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDrivingBehaviourBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_driving_behaviour, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDrivingBehaviourBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.clTotalCountVehicle;
      ConstraintLayout clTotalCountVehicle = ViewBindings.findChildViewById(rootView, id);
      if (clTotalCountVehicle == null) {
        break missingId;
      }

      id = R.id.cvConsolidateVoilationReport;
      CardView cvConsolidateVoilationReport = ViewBindings.findChildViewById(rootView, id);
      if (cvConsolidateVoilationReport == null) {
        break missingId;
      }

      id = R.id.cvDriverCompare;
      CardView cvDriverCompare = ViewBindings.findChildViewById(rootView, id);
      if (cvDriverCompare == null) {
        break missingId;
      }

      id = R.id.cvExcellent;
      CardView cvExcellent = ViewBindings.findChildViewById(rootView, id);
      if (cvExcellent == null) {
        break missingId;
      }

      id = R.id.cvHarshAccelerationReport;
      CardView cvHarshAccelerationReport = ViewBindings.findChildViewById(rootView, id);
      if (cvHarshAccelerationReport == null) {
        break missingId;
      }

      id = R.id.cvHarshBreakReport;
      CardView cvHarshBreakReport = ViewBindings.findChildViewById(rootView, id);
      if (cvHarshBreakReport == null) {
        break missingId;
      }

      id = R.id.cvRanking;
      CardView cvRanking = ViewBindings.findChildViewById(rootView, id);
      if (cvRanking == null) {
        break missingId;
      }

      id = R.id.cvRashTurnReport;
      CardView cvRashTurnReport = ViewBindings.findChildViewById(rootView, id);
      if (cvRashTurnReport == null) {
        break missingId;
      }

      id = R.id.cvReports;
      CardView cvReports = ViewBindings.findChildViewById(rootView, id);
      if (cvReports == null) {
        break missingId;
      }

      id = R.id.cvRiskyDriver;
      CardView cvRiskyDriver = ViewBindings.findChildViewById(rootView, id);
      if (cvRiskyDriver == null) {
        break missingId;
      }

      id = R.id.cvRoute;
      CardView cvRoute = ViewBindings.findChildViewById(rootView, id);
      if (cvRoute == null) {
        break missingId;
      }

      id = R.id.cvTotalVehiclesDetail;
      CardView cvTotalVehiclesDetail = ViewBindings.findChildViewById(rootView, id);
      if (cvTotalVehiclesDetail == null) {
        break missingId;
      }

      id = R.id.driverClassificationChart;
      PieChart driverClassificationChart = ViewBindings.findChildViewById(rootView, id);
      if (driverClassificationChart == null) {
        break missingId;
      }

      id = R.id.drvrComparisonMonthly;
      LinearLayoutCompat drvrComparisonMonthly = ViewBindings.findChildViewById(rootView, id);
      if (drvrComparisonMonthly == null) {
        break missingId;
      }

      id = R.id.drvrToDrvrComparison;
      LinearLayoutCompat drvrToDrvrComparison = ViewBindings.findChildViewById(rootView, id);
      if (drvrToDrvrComparison == null) {
        break missingId;
      }

      id = R.id.fuelChart;
      PieChart fuelChart = ViewBindings.findChildViewById(rootView, id);
      if (fuelChart == null) {
        break missingId;
      }

      id = R.id.ivArrowExcellent;
      ImageView ivArrowExcellent = ViewBindings.findChildViewById(rootView, id);
      if (ivArrowExcellent == null) {
        break missingId;
      }

      id = R.id.ivArrowRisky;
      ImageView ivArrowRisky = ViewBindings.findChildViewById(rootView, id);
      if (ivArrowRisky == null) {
        break missingId;
      }

      id = R.id.ivTotalVehicleIcon;
      AppCompatImageView ivTotalVehicleIcon = ViewBindings.findChildViewById(rootView, id);
      if (ivTotalVehicleIcon == null) {
        break missingId;
      }

      id = R.id.llDrivingLimit;
      LinearLayoutCompat llDrivingLimit = ViewBindings.findChildViewById(rootView, id);
      if (llDrivingLimit == null) {
        break missingId;
      }

      id = R.id.llModerate;
      LinearLayoutCompat llModerate = ViewBindings.findChildViewById(rootView, id);
      if (llModerate == null) {
        break missingId;
      }

      id = R.id.llNoDrivingHours;
      LinearLayoutCompat llNoDrivingHours = ViewBindings.findChildViewById(rootView, id);
      if (llNoDrivingHours == null) {
        break missingId;
      }

      id = R.id.llRiskyDriverReport;
      LinearLayoutCompat llRiskyDriverReport = ViewBindings.findChildViewById(rootView, id);
      if (llRiskyDriverReport == null) {
        break missingId;
      }

      id = R.id.llSafeDriverReports;
      LinearLayoutCompat llSafeDriverReports = ViewBindings.findChildViewById(rootView, id);
      if (llSafeDriverReports == null) {
        break missingId;
      }

      id = R.id.llScore;
      LinearLayoutCompat llScore = ViewBindings.findChildViewById(rootView, id);
      if (llScore == null) {
        break missingId;
      }

      id = R.id.relativeLayout;
      RelativeLayout relativeLayout = ViewBindings.findChildViewById(rootView, id);
      if (relativeLayout == null) {
        break missingId;
      }

      id = R.id.relativeLayoutDC;
      RelativeLayout relativeLayoutDC = ViewBindings.findChildViewById(rootView, id);
      if (relativeLayoutDC == null) {
        break missingId;
      }

      id = R.id.rvExcellentDrivers;
      RecyclerView rvExcellentDrivers = ViewBindings.findChildViewById(rootView, id);
      if (rvExcellentDrivers == null) {
        break missingId;
      }

      id = R.id.rvRiskyDrivers;
      RecyclerView rvRiskyDrivers = ViewBindings.findChildViewById(rootView, id);
      if (rvRiskyDrivers == null) {
        break missingId;
      }

      id = R.id.toolbar;
      View toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }
      ToolbarLayoutBinding binding_toolbar = ToolbarLayoutBinding.bind(toolbar);

      id = R.id.tvDriverAnalysis;
      AppCompatTextView tvDriverAnalysis = ViewBindings.findChildViewById(rootView, id);
      if (tvDriverAnalysis == null) {
        break missingId;
      }

      id = R.id.tvDriverClassification;
      AppCompatTextView tvDriverClassification = ViewBindings.findChildViewById(rootView, id);
      if (tvDriverClassification == null) {
        break missingId;
      }

      id = R.id.tvDriverComparison;
      AppCompatTextView tvDriverComparison = ViewBindings.findChildViewById(rootView, id);
      if (tvDriverComparison == null) {
        break missingId;
      }

      id = R.id.tvDriverScore;
      AppCompatTextView tvDriverScore = ViewBindings.findChildViewById(rootView, id);
      if (tvDriverScore == null) {
        break missingId;
      }

      id = R.id.tvExDriver;
      TextView tvExDriver = ViewBindings.findChildViewById(rootView, id);
      if (tvExDriver == null) {
        break missingId;
      }

      id = R.id.tvFuelHeading;
      AppCompatTextView tvFuelHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvFuelHeading == null) {
        break missingId;
      }

      id = R.id.tvFuelSubHeading;
      AppCompatTextView tvFuelSubHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvFuelSubHeading == null) {
        break missingId;
      }

      id = R.id.tvHarshAcc;
      TextView tvHarshAcc = ViewBindings.findChildViewById(rootView, id);
      if (tvHarshAcc == null) {
        break missingId;
      }

      id = R.id.tvHarshBreak;
      TextView tvHarshBreak = ViewBindings.findChildViewById(rootView, id);
      if (tvHarshBreak == null) {
        break missingId;
      }

      id = R.id.tvHarshOverspeed;
      TextView tvHarshOverspeed = ViewBindings.findChildViewById(rootView, id);
      if (tvHarshOverspeed == null) {
        break missingId;
      }

      id = R.id.tvModerateDriver;
      TextView tvModerateDriver = ViewBindings.findChildViewById(rootView, id);
      if (tvModerateDriver == null) {
        break missingId;
      }

      id = R.id.tvRankingHeading;
      AppCompatTextView tvRankingHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvRankingHeading == null) {
        break missingId;
      }

      id = R.id.tvRashTurn;
      TextView tvRashTurn = ViewBindings.findChildViewById(rootView, id);
      if (tvRashTurn == null) {
        break missingId;
      }

      id = R.id.tvReportsHeading;
      AppCompatTextView tvReportsHeading = ViewBindings.findChildViewById(rootView, id);
      if (tvReportsHeading == null) {
        break missingId;
      }

      id = R.id.tvRiskyDetails;
      AppCompatTextView tvRiskyDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvRiskyDetails == null) {
        break missingId;
      }

      id = R.id.tvRskyDriver;
      TextView tvRskyDriver = ViewBindings.findChildViewById(rootView, id);
      if (tvRskyDriver == null) {
        break missingId;
      }

      id = R.id.tvSeeExcellentDriverDetails;
      AppCompatTextView tvSeeExcellentDriverDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvSeeExcellentDriverDetails == null) {
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

      id = R.id.tvTotalVoilationExDriver;
      TextView tvTotalVoilationExDriver = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalVoilationExDriver == null) {
        break missingId;
      }

      id = R.id.tvTotalVoilationRiskyDrivers;
      TextView tvTotalVoilationRiskyDrivers = ViewBindings.findChildViewById(rootView, id);
      if (tvTotalVoilationRiskyDrivers == null) {
        break missingId;
      }

      id = R.id.vViewExcellent;
      View vViewExcellent = ViewBindings.findChildViewById(rootView, id);
      if (vViewExcellent == null) {
        break missingId;
      }

      id = R.id.vViewRisky;
      View vViewRisky = ViewBindings.findChildViewById(rootView, id);
      if (vViewRisky == null) {
        break missingId;
      }

      return new ActivityDrivingBehaviourBinding((LinearLayoutCompat) rootView, clTotalCountVehicle,
          cvConsolidateVoilationReport, cvDriverCompare, cvExcellent, cvHarshAccelerationReport,
          cvHarshBreakReport, cvRanking, cvRashTurnReport, cvReports, cvRiskyDriver, cvRoute,
          cvTotalVehiclesDetail, driverClassificationChart, drvrComparisonMonthly,
          drvrToDrvrComparison, fuelChart, ivArrowExcellent, ivArrowRisky, ivTotalVehicleIcon,
          llDrivingLimit, llModerate, llNoDrivingHours, llRiskyDriverReport, llSafeDriverReports,
          llScore, relativeLayout, relativeLayoutDC, rvExcellentDrivers, rvRiskyDrivers,
          binding_toolbar, tvDriverAnalysis, tvDriverClassification, tvDriverComparison,
          tvDriverScore, tvExDriver, tvFuelHeading, tvFuelSubHeading, tvHarshAcc, tvHarshBreak,
          tvHarshOverspeed, tvModerateDriver, tvRankingHeading, tvRashTurn, tvReportsHeading,
          tvRiskyDetails, tvRskyDriver, tvSeeExcellentDriverDetails, tvTotalVehicle,
          tvTotalVehicleHeading, tvTotalVoilationExDriver, tvTotalVoilationRiskyDrivers,
          vViewExcellent, vViewRisky);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}