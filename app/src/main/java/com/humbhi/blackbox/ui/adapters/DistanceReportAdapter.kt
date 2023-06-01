package com.humbhi.blackbox.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.DistanceReportAdapterBinding
import com.humbhi.blackbox.ui.data.models.DistanceReportItemData
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.overSpeedDetail.OverspeedDetailActivity
import com.humbhi.blackbox.ui.ui.routePlayback.RoutePlayBack
import com.humbhi.blackbox.ui.utils.ExplicitIntentUtil
import java.text.SimpleDateFormat
import java.util.*

class DistanceReportAdapter(val context: Context,val list:List<DistanceReportItemData>, var startDateParam:String, var endDateParam:String) :
    RecyclerView.Adapter<DistanceReportAdapter.MyHolder>() {

    // Holds the views for adding it to image and text
    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context,list:List<DistanceReportItemData>,startDateParam:String, endDateParam: String) {
            DistanceReportAdapterBinding.bind(itemView).apply {
                tvVehicleName.text = list[adapterPosition].VehicleName
                tvDistanceTravel.text = list[adapterPosition].TotalDistance

                if (list[adapterPosition].getDistanceTravelObject()?.size.toString().equals("null")){
                    ivRoute.visibility = View.GONE
                }
                else{
                    ivRoute.visibility = View.VISIBLE
                    ivRoute.setOnClickListener {
                        val date =  (list[adapterPosition].objTravelReport?.get(0)?.StartDateTime.toString()).split(" ")
                        val backendStartDateParam = date[0]
                        val date1 = (list[adapterPosition].objTravelReport?.get(list[adapterPosition].objTravelReport!!.size-1)?.EndDateTime.toString()).split(" ")
                        val backendEndDateParam = date1[0]
                        val intent = Intent(context, RoutePlayBack::class.java)
                        intent.putExtra("tableName", list[adapterPosition].bbid)
                        intent.putExtra("fromDate", backendStartDateParam)
                        intent.putExtra("endDate", backendEndDateParam)
                        intent.putExtra("vehicleName", list[adapterPosition].VehicleName)
                        intent.putExtra("flag", "DistanceReport")
                        intent.putExtra("showStoppages", "0")
                        context.startActivity(intent)
                    }
                }

            }
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.distance_report_adapter, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.bind(context,list,startDateParam,endDateParam)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }
}