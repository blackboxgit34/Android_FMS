package com.humbhi.blackbox.ui.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class VehicleValueFormater extends ValueFormatter {


        private BarLineChartBase<?> chart;
        public void DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

     public VehicleValueFormater(BarLineChartBase<?> chart) {
        this.chart = chart;
    }
}
