// Generated by view binder compiler. Do not edit!
package com.humbhi.blackbox.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.mikephil.charting.charts.LineChart;
import com.humbhi.blackbox.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMultiLineChartBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final LineChart chart1;

  @NonNull
  public final SeekBar seekBar1;

  @NonNull
  public final SeekBar seekBar2;

  @NonNull
  public final TextView tvXMax;

  @NonNull
  public final TextView tvYMax;

  private ActivityMultiLineChartBinding(@NonNull RelativeLayout rootView, @NonNull LineChart chart1,
      @NonNull SeekBar seekBar1, @NonNull SeekBar seekBar2, @NonNull TextView tvXMax,
      @NonNull TextView tvYMax) {
    this.rootView = rootView;
    this.chart1 = chart1;
    this.seekBar1 = seekBar1;
    this.seekBar2 = seekBar2;
    this.tvXMax = tvXMax;
    this.tvYMax = tvYMax;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMultiLineChartBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMultiLineChartBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_multi_line_chart, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMultiLineChartBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.chart1;
      LineChart chart1 = ViewBindings.findChildViewById(rootView, id);
      if (chart1 == null) {
        break missingId;
      }

      id = R.id.seekBar1;
      SeekBar seekBar1 = ViewBindings.findChildViewById(rootView, id);
      if (seekBar1 == null) {
        break missingId;
      }

      id = R.id.seekBar2;
      SeekBar seekBar2 = ViewBindings.findChildViewById(rootView, id);
      if (seekBar2 == null) {
        break missingId;
      }

      id = R.id.tvXMax;
      TextView tvXMax = ViewBindings.findChildViewById(rootView, id);
      if (tvXMax == null) {
        break missingId;
      }

      id = R.id.tvYMax;
      TextView tvYMax = ViewBindings.findChildViewById(rootView, id);
      if (tvYMax == null) {
        break missingId;
      }

      return new ActivityMultiLineChartBinding((RelativeLayout) rootView, chart1, seekBar1,
          seekBar2, tvXMax, tvYMax);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
