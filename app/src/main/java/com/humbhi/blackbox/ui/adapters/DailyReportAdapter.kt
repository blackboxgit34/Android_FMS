package com.humbhi.blackbox.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.DailyreportAdapterBinding
import com.humbhi.blackbox.ui.data.models.DailyReportResponse
import com.humbhi.blackbox.ui.data.models.ObjDailyReport
import java.util.Locale

class DailyReportAdapter (val context: Context,val list:List<ObjDailyReport>) :
    RecyclerView.Adapter<DailyReportAdapter.MyHolder>() {


    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list: List<ObjDailyReport>) {
            DailyreportAdapterBinding.bind(itemView).apply {
                tvVehicleName.text = list[adapterPosition].VehicleName
                tvSpeed.text = list[adapterPosition].MaxSpeed
                val distance =  String.format(Locale.ENGLISH,"%.2f", list[adapterPosition].Distance.toFloat()).toDouble()
                tvDistance.text = distance.toString()
                tvOverstoppages.text = list[adapterPosition].OverStoppages+" TIMES"
                tvOverspeedings.text = list[adapterPosition].OverSpeedings+" TIMES"
                if (list[adapterPosition].StartLocation.isNullOrEmpty()){
                    tvStartLocation.text = "Location not available"
                }
                else{
                    tvStartLocation.text = list[adapterPosition].StartLocation
                }

                if (list[adapterPosition].StopLocation.isNullOrEmpty()){
                    tvEndLocation.text = "Location not available"
                }
                else{
                    tvEndLocation.text = list[adapterPosition].StopLocation
                }

            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dailyreport_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(context,list)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }
}